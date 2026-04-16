package com.example.gymtop.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.entity.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

/**
 * ExerciseDao - Data Access Object para operações com a tabela ExerciseEntity
 *
 * @Dao: Anotação que marca esta interface como um DAO. Room implementará automaticamente
 * as operações SQL baseado nas anotações e nas queries que escrevemos.
 *
 * Padrão MVVM: O DAO está no Data Layer - responsável apenas por acessar o banco de dados.
 * Repositories usam DAOs, ViewModels usam Repositories.
 */
@Dao
interface ExerciseDao {

    /**
     * Insere um novo exercício (sem séries - as séries são inseridas separadamente)
     * @param exercise: Objeto exercício a ser inserido
     * @return Long: ID do exercício inserido
     */
    @Insert
    suspend fun insert(exercise: ExerciseEntity): Long

    /**
     * Atualiza um exercício existente
     * @param exercise: Objeto exercício com dados atualizados
     */
    @Update
    suspend fun update(exercise: ExerciseEntity)

    /**
     * Deleta um exercício (cascata deleta suas séries)
     * @param exercise: Objeto exercício a ser deletado
     */
    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    /**
     * Obtém todos os exercícios de um treino com suas séries relacionadas
     * @param workoutId: ID do treino
     * Retorna um Flow que emite quando há mudanças
     */
    @Transaction
    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY id ASC")
    fun getExercisesWithSetsByWorkoutId(workoutId: Long): Flow<List<ExerciseWithSets>>

    /**
     * Obtém um exercício específico com suas séries relacionadas
     * @param exerciseId: ID do exercício
     */
    @Transaction
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseWithSetsById(exerciseId: Long): ExerciseWithSets?

    /**
     * Obtém um exercício sem as séries (mais leve se não precisar dos dados das séries)
     * @param exerciseId: ID do exercício
     */
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): ExerciseEntity?

    /**
     * Deleta todos os exercícios de um treino (cascata deleta suas séries)
     */
    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Long)

    /**
     * Conta quantos exercícios um treino tem
     */
    @Query("SELECT COUNT(*) FROM exercises WHERE workoutId = :workoutId")
    suspend fun countExercisesByWorkoutId(workoutId: Long): Int
}