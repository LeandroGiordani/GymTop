package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.SetDao
import com.example.gymtop.data.entity.SetEntity
import kotlinx.coroutines.flow.Flow

/**
 * SetRepository - Repositório que abstrai o acesso aos dados de SetEntity
 *
 * Padrão Repository + MVVM:
 * Centraliza lógica de acesso aos dados de séries (sets).
 * ViewModels usam este repositório para operações com séries.
 *
 * @param setDao: DAO injetado via Hilt
 */
class SetRepository(
    private val setDao: SetDao
) {

    /**
     * Obtém todas as séries de um exercício como um Flow reativo
     * @param exerciseId: ID do exercício
     */
    fun getSetsByExerciseId(exerciseId: Long): Flow<List<SetEntity>> {
        return setDao.getSetsByExerciseId(exerciseId)
    }

    /**
     * Insere uma nova série de forma assíncrona
     * @param set: Objeto série a ser inserido
     * @return Long: ID da série inserida
     */
    suspend fun insertSet(set: SetEntity): Long {
        // TODO: Adicionar validação de dados
        // Exemplo: verificar se reps/duration > 0, weight >= 0, etc.
        return setDao.insert(set)
    }

    /**
     * Insere múltiplas séries de uma vez (útil ao criar exercício com vários sets)
     * @param sets: Lista de séries a inserir
     * @return List<Long>: IDs das séries inseridas
     */
    suspend fun insertMultipleSets(sets: List<SetEntity>): List<Long> {
        // TODO: Validar que setNumber é sequencial (1, 2, 3...)
        return setDao.insertMultiple(sets)
    }

    /**
     * Atualiza uma série existente
     * @param set: Objeto série com dados atualizados
     */
    suspend fun updateSet(set: SetEntity) {
        // TODO: Adicionar validação de dados
        setDao.update(set)
    }

    /**
     * Deleta uma série
     * @param set: Objeto série a ser deletado
     */
    suspend fun deleteSet(set: SetEntity) {
        // TODO: Reordenar setNumbers das séries restantes?
        setDao.delete(set)
    }

    /**
     * Obtém uma série específica pelo ID
     * @param setId: ID da série
     */
    suspend fun getSetById(setId: Long): SetEntity? {
        return setDao.getSetById(setId)
    }

    /**
     * Deleta todas as séries de um exercício
     * @param exerciseId: ID do exercício
     */
    suspend fun deleteSetsByExerciseId(exerciseId: Long) {
        setDao.deleteSetsByExerciseId(exerciseId)
    }

    /**
     * Obtém o número da próxima série para um exercício
     * Útil para UI quando usuário quer adicionar uma nova série
     * @param exerciseId: ID do exercício
     */
    suspend fun getNextSetNumber(exerciseId: Long): Int {
        return setDao.getMaxSetNumber(exerciseId) + 1
    }
}
