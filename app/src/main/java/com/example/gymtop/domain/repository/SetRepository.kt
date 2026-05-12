package com.example.gymtop.domain.repository

import com.example.gymtop.data.entity.SetEntity
import kotlinx.coroutines.flow.Flow

/**
 * SetRepository — contrato da camada de domínio para acesso a dados de séries (sets).
 *
 * A interface desacopla a lógica de negócio da implementação concreta ([SetRepositoryImpl]).
 * O ViewModel depende DESTA interface, permitindo:
 * - Trocar Room por outra fonte de dados sem alterar ViewModels
 * - Criar um FakeSetRepository para testes unitários
 * - Reutilizar o contrato em Kotlin Multiplatform
 *
 * Nota: Sets ainda são representados como [SetEntity] nesta interface pois não existe
 * um domain model dedicado para séries isoladas. Elas são consumidas pela UI sempre
 * como [SetType] dentro de [Exercise], após mapeamento feito pelo [ExerciseRepository].
 * Quando (e se) um caso de uso exigir manipulação de sets isolados na UI,
 * considere criar um domain model "ExerciseSet" e migrar esta interface.
 */
interface SetRepository {

    /**
     * Observa todas as séries de um exercício como um Flow reativo.
     * Emite nova lista automaticamente quando o banco é alterado.
     * @param exerciseId ID do exercício.
     */
    fun getSetsByExerciseId(exerciseId: Long): Flow<List<SetEntity>>

    /**
     * Insere uma nova série.
     * @param set Série a ser persistida.
     * @return Long ID gerado pelo banco.
     */
    suspend fun insertSet(set: SetEntity): Long

    /**
     * Insere múltiplas séries de uma vez (útil ao criar exercício com vários sets).
     * @param sets Lista de séries a inserir.
     * @return List<Long> IDs das séries inseridas.
     */
    suspend fun insertMultipleSets(sets: List<SetEntity>): List<Long>

    /**
     * Atualiza uma série existente.
     * @param set Série com dados atualizados.
     */
    suspend fun updateSet(set: SetEntity)

    /**
     * Deleta uma série.
     * @param set Série a ser removida.
     */
    suspend fun deleteSet(set: SetEntity)

    /**
     * Busca uma série específica pelo ID.
     * @param setId ID da série.
     * @return A série encontrada ou null.
     */
    suspend fun getSetById(setId: Long): SetEntity?

    /**
     * Deleta todas as séries de um exercício.
     * @param exerciseId ID do exercício.
     */
    suspend fun deleteSetsByExerciseId(exerciseId: Long)

    /**
     * Retorna o número da próxima série para um exercício.
     * Útil na UI quando o usuário quer adicionar uma nova série sequencial.
     * @param exerciseId ID do exercício.
     */
    suspend fun getNextSetNumber(exerciseId: Long): Int
}
