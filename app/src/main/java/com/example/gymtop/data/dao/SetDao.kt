package com.example.gymtop.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymtop.data.entity.SetEntity
import kotlinx.coroutines.flow.Flow

/**
 * SetDao - Data Access Object para operações com a tabela SetEntity
 *
 * Gerencia as séries individuais de cada exercício.
 * Cada série pode ter diferentes reps, duration ou weight.
 */
@Dao
interface SetDao {

    /**
     * Insere uma nova série
     * @param set: Objeto série a ser inserido
     * @return Long: ID da série inserida
     */
    @Insert
    suspend fun insert(set: SetEntity): Long

    /**
     * Insere múltiplas séries em uma transação
     * @param sets: Lista de séries a inserir
     */
    @Insert
    suspend fun insertMultiple(sets: List<SetEntity>): List<Long>

    /**
     * Atualiza uma série existente
     * @param set: Objeto série com dados atualizados
     */
    @Update
    suspend fun update(set: SetEntity)

    /**
     * Deleta uma série
     * @param set: Objeto série a ser deletado
     */
    @Delete
    suspend fun delete(set: SetEntity)

    /**
     * Obtém todas as séries de um exercício específico, ordenadas por número da série
     * @param exerciseId: ID do exercício
     */
    @Query("SELECT * FROM sets WHERE exerciseId = :exerciseId ORDER BY setNumber ASC")
    fun getSetsByExerciseId(exerciseId: Long): Flow<List<SetEntity>>

    /**
     * Obtém uma série específica pelo ID
     */
    @Query("SELECT * FROM sets WHERE id = :setId")
    suspend fun getSetById(setId: Long): SetEntity?

    /**
     * Deleta todas as séries de um exercício (útil quando deletar exercício)
     */
    @Query("DELETE FROM sets WHERE exerciseId = :exerciseId")
    suspend fun deleteSetsByExerciseId(exerciseId: Long)

    /**
     * Obtém o número máximo de série para um exercício (para determinar próximo número)
     */
    @Query("SELECT COALESCE(MAX(setNumber), 0) FROM sets WHERE exerciseId = :exerciseId")
    suspend fun getMaxSetNumber(exerciseId: Long): Int
}
