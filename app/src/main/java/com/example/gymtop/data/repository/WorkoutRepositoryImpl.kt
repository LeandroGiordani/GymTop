package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.WorkoutDao
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.mapper.toDomain
import com.example.gymtop.data.mapper.toEntity
import com.example.gymtop.domain.model.Workout
import com.example.gymtop.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * WorkoutRepositoryImpl — implementação concreta de [WorkoutRepository].
 *
 * Responsabilidade: intermediar o acesso ao banco de dados Room via [WorkoutDao],
 * traduzindo entre entidades ([WorkoutEntity]) e modelos de domínio ([Workout]).
 *
 * Por que separar interface de implementação?
 * - ViewModels dependem de [WorkoutRepository] (interface do domain), não desta classe.
 * - Isso permite criar um FakeWorkoutRepository para testes unitários sem banco de dados.
 * - Facilita substituir Room por outra fonte de dados (Firebase, API) sem tocar em ViewModels.
 * - Segue o Dependency Inversion Principle (SOLID).
 *
 * @param workoutDao DAO injetado via Hilt (fornecido por DatabaseModule).
 */
class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    /**
     * Obtém todos os treinos como um Flow reativo.
     * Quando o banco de dados é atualizado, o Flow emite os novos dados automaticamente.
     */
    override fun getAllWorkouts(): Flow<List<Workout>> {
        return workoutDao.getAllWorkouts().map { list -> list.map { entity -> entity.toDomain() } }
    }

    /**
     * Insere um novo treino de forma assíncrona.
     * @param workout Objeto treino a ser inserido.
     */
    override suspend fun insertWorkout(workout: Workout) {
        // TODO: Adicionar validação de dados (nome não vazio, etc.)
        workoutDao.insert(workout.toEntity())
    }

    /**
     * Atualiza um treino existente.
     * @param workout Objeto treino com dados atualizados.
     */
    override suspend fun updateWorkout(workout: Workout) {
        // TODO: Adicionar validação de dados
        workoutDao.update(workout.toEntity())
    }

    /**
     * Deleta um treino.
     * @param workout Objeto treino a ser deletado.
     */
    override suspend fun deleteWorkout(workout: Workout) {
        // TODO: Adicionar logging de deleção se necessário
        workoutDao.delete(workout.toEntity())
    }

    /**
     * Busca um treino específico pelo ID.
     * @param workoutId ID do treino.
     * @return O treino encontrado como domain model, ou null se não existir.
     */
    override suspend fun getWorkoutById(workoutId: Long): Workout? {
        // TODO: Implementar cache se necessário
        return workoutDao.getWorkoutById(workoutId)?.toDomain()
    }
}
