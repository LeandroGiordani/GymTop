package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.datasource.LibraryDataSource
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.mapper.toDomain
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ExerciseRepositoryImpl — implementação concreta de [ExerciseRepository].
 *
 * Responsabilidade central: combinar duas fontes de dados para montar o objeto Exercise (domain):
 *   - [ExerciseDao] (Room/SQLite) → dados do usuário: id, workoutId, type, sets
 *   - [LibraryDataSource] (JSON em memória) → dados do catálogo: nome, grupo muscular, etc.
 *
 * Por que separar interface de implementação?
 * - ViewModels dependem de [ExerciseRepository] (interface do domain), não desta classe.
 * - Isso permite criar um FakeExerciseRepository para testes sem banco de dados ou JSON.
 * - Facilita trocar as fontes de dados no futuro sem impactar ViewModels.
 * - Segue o Dependency Inversion Principle (SOLID).
 *
 * @param exerciseDao       DAO para operações no banco (injetado via Hilt).
 * @param libraryDataSource Fonte em memória do catálogo JSON (injetado via Hilt).
 */
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val libraryDataSource: LibraryDataSource
) : ExerciseRepository {

    /**
     * Observa todos os exercícios de um treino com suas séries.
     *
     * Como funciona o merge:
     * 1. DAO retorna Flow<List<ExerciseWithSets>> (banco de dados)
     * 2. Para cada ExerciseWithSets, busca o LibraryExercise via findById()
     * 3. Chama toDomain(libraryExercise) para montar o Exercise completo
     *
     * Por que buildList em vez de mapNotNull?
     * findById() é uma suspend fun — lambdas padrão (mapNotNull, map) não aceitam suspend.
     * O bloco lambda de Flow.map{} SIM aceita suspend, então usamos buildList + for loop.
     *
     * Exercícios cujo libraryExerciseId não exista no catálogo são ignorados (continue).
     */
    override fun getExercisesByWorkoutId(workoutId: Long): Flow<List<Exercise>> {
        return exerciseDao.getExercisesWithSetsByWorkoutId(workoutId)
            .map { exercisesWithSets ->
                buildList {
                    for (withSets in exercisesWithSets) {
                        val lib = libraryDataSource.findById(
                            withSets.exerciseEntity.libraryExerciseId
                        ) ?: continue
                        add(withSets.toDomain(lib))
                    }
                }
            }
    }

    /**
     * Insere um novo exercício no banco.
     * @param exercise ExerciseEntity com workoutId e libraryExerciseId preenchidos.
     * @return Long ID gerado pelo banco.
     */
    override suspend fun insertExercise(exercise: ExerciseEntity): Long {
        // TODO: Adicionar validação (workoutId válido, libraryExerciseId não vazio)
        return exerciseDao.insert(exercise)
    }

    /**
     * Atualiza um exercício existente.
     * @param exercise ExerciseEntity com dados atualizados.
     */
    override suspend fun updateExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar validação
        exerciseDao.update(exercise)
    }

    /**
     * Deleta um exercício — CASCADE no banco apaga as SetEntities automaticamente.
     * @param exercise ExerciseEntity a ser deletado.
     */
    override suspend fun deleteExercise(exercise: ExerciseEntity) {
        exerciseDao.delete(exercise)
    }

    /**
     * Busca um único exercício com séries, mesclado com dados da biblioteca.
     * @param exerciseId ID do exercício no banco.
     * @return Exercise (domain) ou null se não encontrado ou sem catálogo.
     */
    override suspend fun getExerciseById(exerciseId: Long): Exercise? {
        val withSets = exerciseDao.getExerciseWithSetsById(exerciseId) ?: return null
        val lib = libraryDataSource.findById(withSets.exerciseEntity.libraryExerciseId)
            ?: return null
        return withSets.toDomain(lib)
    }

    /**
     * Busca a entidade pura sem séries e sem dados da biblioteca.
     * Útil para operações de update/delete que só precisam do objeto persistido.
     * @param exerciseId ID do exercício.
     */
    override suspend fun getExerciseEntityById(exerciseId: Long): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    /**
     * Deleta todos os exercícios de um treino (e suas séries via CASCADE).
     * @param workoutId ID do treino.
     */
    override suspend fun deleteExercisesByWorkoutId(workoutId: Long) {
        exerciseDao.deleteExercisesByWorkoutId(workoutId)
    }

    /**
     * Conta quantos exercícios um treino possui.
     * @param workoutId ID do treino.
     */
    override suspend fun countExercisesByWorkoutId(workoutId: Long): Int {
        return exerciseDao.countExercisesByWorkoutId(workoutId)
    }
}
