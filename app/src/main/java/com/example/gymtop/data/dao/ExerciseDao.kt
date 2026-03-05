package com.example.gymtop.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymtop.data.entity.ExerciseEntity
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
     * Insere um novo exercício no banco de dados
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
     * Deleta um exercício
     * @param exercise: Objeto exercício a ser deletado
     */
    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    /**
     * Obtém todos os exercícios de um treino específico
     * @param workoutId: ID do treino
     * Retorna um Flow que emite automaticamente quando há mudanças
     */
    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY id ASC")
    fun getExercisesByWorkoutId(workoutId: Int): Flow<List<ExerciseEntity>>

    /**
     * Obtém um exercício específico pelo ID
     */
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): ExerciseEntity?

    /**
     * TODO: Implementar queries adicionais conforme necessário
     * - Deletar todos os exercícios de um treino
     * - Contar exercícios por treino
     * - Filtrar por nome do exercício
     * etc.
     */
}

