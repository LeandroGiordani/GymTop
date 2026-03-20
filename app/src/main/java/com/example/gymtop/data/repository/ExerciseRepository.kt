package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ExerciseRepository - Repositório que abstrai o acesso aos dados de ExerciseEntity
 *
 * Agora retorna Exercises do domain (com séries mapeadas) ao invés de ExerciseEntity bruto.
 *
 * Padrão MVVM:
 * Repository está no Data Layer mas é usado pela Presentation Layer (ViewModels).
 *
 * @param exerciseDao: DAO injetado via Hilt
 */
class ExerciseRepository(
    private val exerciseDao: ExerciseDao
) {

    /**
     * Obtém todos os exercícios de um treino com suas séries, já mapeados para o domain model
     * @param workoutId: ID do treino ao qual os exercícios pertencem
     * @return Flow de Exercises do domain (já contém SetType mapeados)
     */
    fun getExercisesByWorkoutId(workoutId: Long): Flow<List<Exercise>> {
        return exerciseDao.getExercisesWithSetsByWorkoutId(workoutId)
            .map { exercisesWithSets ->
                exercisesWithSets.map { it.toDomain() }
            }
    }

    /**
     * Insere um novo exercício de forma assíncrona
     * Nota: Séries devem ser inseridas separadamente via SetRepository
     * @param exercise: Objeto exercício a ser inserido
     * @return Long: ID do exercício inserido
     */
    suspend fun insertExercise(exercise: ExerciseEntity): Long {
        // TODO: Adicionar validação de dados
        // Exemplo: verificar se nome não está vazio, workoutId é válido, etc.
        return exerciseDao.insert(exercise)
    }

    /**
     * Atualiza um exercício existente
     * @param exercise: Objeto exercício com dados atualizados
     */
    suspend fun updateExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar validação de dados
        exerciseDao.update(exercise)
    }

    /**
     * Deleta um exercício (cascata deleta suas séries também)
     * @param exercise: Objeto exercício a ser deletado
     */
    suspend fun deleteExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar confirmação antes de deletar (se necessário)
        exerciseDao.delete(exercise)
    }

    /**
     * Obtém um exercício específico com todas suas séries, mapeado para domain model
     * @param exerciseId: ID do exercício
     */
    suspend fun getExerciseById(exerciseId: Long): Exercise? {
        return exerciseDao.getExerciseWithSetsById(exerciseId)?.toDomain()
    }

    /**
     * Obtém um exercício sem as séries (mais leve se não precisar dos dados das séries)
     * @param exerciseId: ID do exercício
     */
    suspend fun getExerciseEntityById(exerciseId: Long): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    /**
     * Deleta todos os exercícios de um treino (cascata deleta suas séries)
     * @param workoutId: ID do treino
     */
    suspend fun deleteExercisesByWorkoutId(workoutId: Long) {
        // TODO: Adicionar logs para auditoria
        exerciseDao.deleteExercisesByWorkoutId(workoutId)
    }

    /**
     * Conta quantos exercícios um treino tem
     * @param workoutId: ID do treino
     */
    suspend fun countExercisesByWorkoutId(workoutId: Long): Int {
        return exerciseDao.countExercisesByWorkoutId(workoutId)
    }
}
