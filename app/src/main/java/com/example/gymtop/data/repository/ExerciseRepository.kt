package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.datasource.LibraryDataSource
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ExerciseRepository - Repositório que abstrai o acesso aos dados de exercícios.
 *
 * Responsabilidade central deste Repository:
 * Combinar duas fontes de dados para montar o objeto Exercise (domain model):
 *
 *   - ExerciseDao (Room/SQLite) → dados do usuário: id, workoutId, type, sets
 *   - LibraryDataSource (JSON em memória) → dados do catálogo: nome, grupo muscular, etc.
 *
 * A UI (ViewModel) nunca acessa ExerciseDao ou LibraryDataSource diretamente.
 * Sempre consome Exercise (domain) pronto.
 *
 * Padrão MVVM: Repository está no Data Layer. ViewModels dependem desta classe,
 * não do DAO ou do DataSource.
 *
 * @param exerciseDao       DAO para operações no banco (injetado via Hilt)
 * @param libraryDataSource fonte em memória do catálogo JSON (injetado via Hilt)
 */
class ExerciseRepository(
    private val exerciseDao: ExerciseDao,
    private val libraryDataSource: LibraryDataSource
) {

    /**
     * Observa todos os exercícios de um treino com suas séries.
     *
     * Como funciona o merge:
     * 1. DAO retorna Flow<List<ExerciseWithSets>> (banco de dados)
     * 2. Para cada ExerciseWithSets, busca o LibraryExercise pelo libraryExerciseId
     * 3. Chama toDomain(libraryExercise) para montar o Exercise completo
     *
     * Exercícios cujo libraryExerciseId não exista no catálogo são ignorados (mapNotNull).
     * Isso não deve ocorrer em uso normal, mas evita crashes se o JSON mudar.
     *
     * @param workoutId ID do treino
     * @return Flow reativo — emite nova lista quando banco é alterado
     */
    fun getExercisesByWorkoutId(workoutId: Long): Flow<List<Exercise>> {
        return exerciseDao.getExercisesWithSetsByWorkoutId(workoutId)
            .map { exercisesWithSets ->
                exercisesWithSets.mapNotNull { withSets ->
                    // Lookup O(1) no HashMap em memória
                    val lib = libraryDataSource.findById(withSets.exerciseEntity.libraryExerciseId)
                        ?: return@mapNotNull null
                    withSets.toDomain(lib)
                }
            }
    }

    /**
     * Insere um novo exercício no banco.
     * Nota: As séries devem ser inseridas separadamente via SetRepository.
     *
     * @param exercise ExerciseEntity pronto para persistir
     * @return Long ID gerado pelo banco
     */
    suspend fun insertExercise(exercise: ExerciseEntity): Long {
        // TODO: Adicionar validação (workoutId válido, libraryExerciseId não vazio)
        return exerciseDao.insert(exercise)
    }

    /**
     * Atualiza um exercício existente (ex: mudar o type padrão).
     * @param exercise ExerciseEntity com dados atualizados
     */
    suspend fun updateExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar validação
        exerciseDao.update(exercise)
    }

    /**
     * Deleta um exercício — o CASCADE do ForeignKey apaga as SetEntities automaticamente.
     * @param exercise ExerciseEntity a ser deletado
     */
    suspend fun deleteExercise(exercise: ExerciseEntity) {
        exerciseDao.delete(exercise)
    }

    /**
     * Busca um único exercício com suas séries pelo ID, já com dados da biblioteca mesclados.
     * @param exerciseId ID do exercício no banco
     * @return Exercise (domain) ou null se não encontrado
     */
    suspend fun getExerciseById(exerciseId: Long): Exercise? {
        val withSets = exerciseDao.getExerciseWithSetsById(exerciseId) ?: return null
        val lib = libraryDataSource.findById(withSets.exerciseEntity.libraryExerciseId)
            ?: return null
        return withSets.toDomain(lib)
    }

    /**
     * Busca a entidade pura sem as séries e sem os dados da biblioteca.
     * Útil para operações de update/delete que só precisam do objeto persistido.
     *
     * @param exerciseId ID do exercício
     */
    suspend fun getExerciseEntityById(exerciseId: Long): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    /**
     * Deleta todos os exercícios de um treino (cascata deleta suas séries).
     * @param workoutId ID do treino
     */
    suspend fun deleteExercisesByWorkoutId(workoutId: Long) {
        exerciseDao.deleteExercisesByWorkoutId(workoutId)
    }

    /**
     * Conta quantos exercícios um treino tem.
     * @param workoutId ID do treino
     */
    suspend fun countExercisesByWorkoutId(workoutId: Long): Int {
        return exerciseDao.countExercisesByWorkoutId(workoutId)
    }
}
