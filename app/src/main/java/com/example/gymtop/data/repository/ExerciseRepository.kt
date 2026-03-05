package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.ExerciseDao
import com.example.gymtop.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

/**
 * ExerciseRepository - Repositório que abstrai o acesso aos dados de ExerciseEntity
 *
 * Padrão Repository:
 * O Repository atua como intermediário entre a UI/ViewModels e o Data Layer (DAOs).
 * Centraliza lógica de acesso aos dados e permite trocar a fonte de dados
 * sem impactar o resto da aplicação.
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
     * Obtém todos os exercícios de um treino específico como um Flow reativo
     * @param workoutId: ID do treino ao qual os exercícios pertencem
     */
    fun getExercisesByWorkoutId(workoutId: Int): Flow<List<ExerciseEntity>> {
        // TODO: Implementar lógica adicional se necessário
        return exerciseDao.getExercisesByWorkoutId(workoutId)
    }

    /**
     * Insere um novo exercício de forma assíncrona
     * @param exercise: Objeto exercício a ser inserido
     */
    suspend fun insertExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar validação de dados
        // Exemplo: verificar se nome não está vazio, se sets/reps > 0, etc.
        exerciseDao.insert(exercise)
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
     * Deleta um exercício
     * @param exercise: Objeto exercício a ser deletado
     */
    suspend fun deleteExercise(exercise: ExerciseEntity) {
        // TODO: Adicionar confirmação antes de deletar (se necessário)
        exerciseDao.delete(exercise)
    }

    /**
     * Obtém um exercício específico pelo ID
     * @param exerciseId: ID do exercício
     */
    suspend fun getExerciseById(exerciseId: Int): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário
     * - searchExercises(query: String)
     * - deleteExercisesByWorkoutId(workoutId: Int)
     * - getExerciseStats(exerciseId: Int)
     * etc.
     */
}

