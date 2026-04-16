package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.WorkoutDao
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.mapper.toDomain
import com.example.gymtop.data.mapper.toEntity
import com.example.gymtop.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * WorkoutRepository - Repositório que abstrai o acesso aos dados de WorkoutEntity
 *
 * Padrão Repository:
 * O Repository atua como intermediário entre a UI/ViewModels e o Data Layer (DAOs).
 * Ele centraliza a lógica de acesso aos dados e permite trocar a fonte de dados
 * sem impactar o resto da aplicação (exemplo: trocar Room por API do servidor).
 *
 * Padrão MVVM:
 * Repository está no Data Layer mas é usado pela Presentation Layer (ViewModels).
 * Responsabilidades:
 * - Centralizar lógica de acesso a dados
 * - Permitir trocar fonte de dados sem quebrar ViewModels
 * - Preparar dados para a UI (transformações, conversões)
 *
 * @param workoutDao: DAO injetado via Hilt
 */
class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {

    /**
     * Obtém todos os treinos como um Flow reativo
     * Quando o banco de dados é atualizado, o Flow emite os novos dados automaticamente
     */
    fun getAllWorkouts(): Flow<List<Workout>> {
        // TODO: Implementar lógica adicional se necessário
        // Exemplo: transformação de dados, logging, tratamento de erros
        return workoutDao.getAllWorkouts().map { list -> list.map { entity -> entity.toDomain() } }
    }

    /**
     * Insere um novo treino de forma assíncrona
     * @param workout: Objeto treino a ser inserido
     */
    suspend fun insertWorkout(workout: Workout) {
        // TODO: Adicionar validação de dados
        // Exemplo: verificar se nome não está vazio, se data é válida, etc.
        workoutDao.insert(workout.toEntity())
    }

    /**
     * Atualiza um treino existente
     * @param workout: Objeto treino com dados atualizados
     */
    suspend fun updateWorkout(workout: Workout) {
        // TODO: Adicionar validação de dados
        workoutDao.update(workout.toEntity())
    }

    /**
     * Deleta um treino
     * @param workout: Objeto treino a ser deletado
     */
    suspend fun deleteWorkout(workout: Workout) {
        // TODO: Adicionar confirmação antes de deletar (se necessário)
        // TODO: Adicionar logging de deleção
        workoutDao.delete(workout.toEntity())
    }

    /**
     * Obtém um treino específico pelo ID
     * @param workoutId: ID do treino
     */
    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity? {
        // TODO: Implementar cache se necessário
        return workoutDao.getWorkoutById(workoutId)
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário
     * - searchWorkouts(query: String)
     * - getWorkoutsByDateRange(startDate: Long, endDate: Long)
     * - deleteAllWorkouts()
     * etc.
     */
}
