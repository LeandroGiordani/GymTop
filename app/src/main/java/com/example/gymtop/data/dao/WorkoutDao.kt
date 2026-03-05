package com.example.gymtop.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymtop.data.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

/**
 * WorkoutDao - Data Access Object para operações com a tabela WorkoutEntity
 *
 * @Dao: Anotação que marca esta interface como um DAO. Room implementará automaticamente
 * as operações SQL baseado nas anotações e nas queries que escrevemos.
 *
 * Padrão MVVM: O DAO está no Data Layer - responsável apenas por acessar o banco de dados.
 * Repositories usam DAOs, ViewModels usam Repositories.
 *
 * Flow: Observável reativo que emite novos valores quando os dados mudam no banco.
 * Quando o banco é atualizado, qualquer listener automáticamente recebe os novos dados.
 */
@Dao
interface WorkoutDao {

    /**
     * Insere um novo treino no banco de dados
     * @param workout: Objeto treino a ser inserido
     * @return Long: ID do treino inserido
     */
    @Insert
    suspend fun insert(workout: WorkoutEntity): Long

    /**
     * Atualiza um treino existente
     * @param workout: Objeto treino com dados atualizados
     */
    @Update
    suspend fun update(workout: WorkoutEntity)

    /**
     * Deleta um treino
     * @param workout: Objeto treino a ser deletado
     */
    @Delete
    suspend fun delete(workout: WorkoutEntity)

    /**
     * Obtém todos os treinos ordenados por data (descendente - mais recentes primeiro)
     * Retorna um Flow que emite automaticamente quando há mudanças
     */
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    /**
     * Obtém um treino específico pelo ID
     */
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity?

    /**
     * TODO: Implementar queries adicionais conforme necessário
     * - Filtrar por data
     * - Filtrar por nome
     * - Contar total de treinos
     * etc.
     */
}

