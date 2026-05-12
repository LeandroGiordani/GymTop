package com.example.gymtop.domain.repository

import com.example.gymtop.domain.model.Workout
import kotlinx.coroutines.flow.Flow

/**
 * WorkoutRepository — contrato da camada de domínio para acesso a dados de treinos.
 *
 * A interface desacopla a lógica de negócio da implementação concreta (Room/SQLite).
 * O ViewModel depende DESTA interface, não de [WorkoutRepositoryImpl], permitindo:
 * - Trocar Room por outra fonte de dados (API, Firebase) sem alterar ViewModels
 * - Criar um FakeWorkoutRepository para testes unitários sem banco de dados
 * - Reutilizar o contrato em Kotlin Multiplatform (iOS pode ter sua própria implementação)
 *
 * Todas as operações de escrita são suspend functions pois envolvem I/O de disco.
 * getAllWorkouts() retorna Flow para emissão reativa quando o banco é atualizado.
 */
interface WorkoutRepository {

    /**
     * Observa todos os treinos como um Flow reativo.
     * Emite nova lista automaticamente sempre que o banco de dados é alterado.
     */
    fun getAllWorkouts(): Flow<List<Workout>>

    /**
     * Insere um novo treino.
     * @param workout Objeto treino a ser persistido.
     */
    suspend fun insertWorkout(workout: Workout)

    /**
     * Atualiza um treino existente.
     * @param workout Objeto treino com dados atualizados.
     */
    suspend fun updateWorkout(workout: Workout)

    /**
     * Deleta um treino.
     * @param workout Objeto treino a ser removido.
     */
    suspend fun deleteWorkout(workout: Workout)

    /**
     * Busca um treino específico pelo ID.
     * @param workoutId ID do treino.
     * @return O treino encontrado ou null se não existir.
     */
    suspend fun getWorkoutById(workoutId: Long): Workout?
}
