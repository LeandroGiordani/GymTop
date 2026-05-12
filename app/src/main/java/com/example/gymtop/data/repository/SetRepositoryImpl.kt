package com.example.gymtop.data.repository

import com.example.gymtop.data.dao.SetDao
import com.example.gymtop.data.entity.SetEntity
import com.example.gymtop.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * SetRepositoryImpl — implementação concreta de [SetRepository].
 *
 * Responsabilidade: intermediar o acesso ao banco de dados Room via [SetDao],
 * delegando operações de persistência de séries (sets).
 *
 * Por que separar interface de implementação?
 * - ViewModels dependem de [SetRepository] (interface do domain), não desta classe.
 * - Isso permite criar um FakeSetRepository para testes unitários sem banco de dados.
 * - Facilita substituir Room por outra fonte de dados sem tocar em ViewModels.
 * - Segue o Dependency Inversion Principle (SOLID).
 *
 * @param setDao DAO injetado via Hilt (fornecido por DatabaseModule).
 */
class SetRepositoryImpl @Inject constructor(
    private val setDao: SetDao
) : SetRepository {

    /**
     * Obtém todas as séries de um exercício como um Flow reativo.
     * @param exerciseId ID do exercício.
     */
    override fun getSetsByExerciseId(exerciseId: Long): Flow<List<SetEntity>> {
        return setDao.getSetsByExerciseId(exerciseId)
    }

    /**
     * Insere uma nova série de forma assíncrona.
     * @param set Objeto série a ser inserido.
     * @return Long ID da série inserida.
     */
    override suspend fun insertSet(set: SetEntity): Long {
        // TODO: Adicionar validação de dados (reps/duration > 0, weight >= 0)
        return setDao.insert(set)
    }

    /**
     * Insere múltiplas séries de uma vez (útil ao criar exercício com vários sets).
     * @param sets Lista de séries a inserir.
     * @return List<Long> IDs das séries inseridas.
     */
    override suspend fun insertMultipleSets(sets: List<SetEntity>): List<Long> {
        // TODO: Validar que setNumber é sequencial (1, 2, 3...)
        return setDao.insertMultiple(sets)
    }

    /**
     * Atualiza uma série existente.
     * @param set Objeto série com dados atualizados.
     */
    override suspend fun updateSet(set: SetEntity) {
        // TODO: Adicionar validação de dados
        setDao.update(set)
    }

    /**
     * Deleta uma série.
     * @param set Objeto série a ser deletado.
     */
    override suspend fun deleteSet(set: SetEntity) {
        // TODO: Reordenar setNumbers das séries restantes se necessário
        setDao.delete(set)
    }

    /**
     * Obtém uma série específica pelo ID.
     * @param setId ID da série.
     * @return A série encontrada ou null.
     */
    override suspend fun getSetById(setId: Long): SetEntity? {
        return setDao.getSetById(setId)
    }

    /**
     * Deleta todas as séries de um exercício.
     * @param exerciseId ID do exercício.
     */
    override suspend fun deleteSetsByExerciseId(exerciseId: Long) {
        setDao.deleteSetsByExerciseId(exerciseId)
    }

    /**
     * Obtém o número da próxima série para um exercício.
     * @param exerciseId ID do exercício.
     */
    override suspend fun getNextSetNumber(exerciseId: Long): Int {
        return setDao.getMaxSetNumber(exerciseId) + 1
    }
}
