package com.example.gymtop.domain.repository

import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

/**
 * ExerciseRepository — contrato da camada de domínio para acesso a dados de exercícios.
 *
 * A interface desacopla a lógica de negócio da implementação concreta
 * ([ExerciseRepositoryImpl]), que combina duas fontes:
 *  - ExerciseDao (Room/SQLite) — dados do usuário: id, workoutId, sets, etc.
 *  - LibraryDataSource (JSON em memória) — dados do catálogo: nome, grupo muscular, etc.
 *
 * O ViewModel depende DESTA interface, permitindo:
 * - Trocar as fontes de dados sem impactar ViewModels
 * - Criar um FakeExerciseRepository para testes unitários
 * - Reutilizar o contrato em Kotlin Multiplatform
 *
 * Leitura: retorna [Exercise] (domain) — UI nunca precisa importar classes de dados.
 * Escrita: ainda usa [ExerciseEntity] pois não existe um domain model de escrita para exercícios.
 * TODO: Criar um domain model de entrada (ex: NewExercise) para remover dependência de ExerciseEntity.
 */
interface ExerciseRepository {

    /**
     * Observa todos os exercícios de um treino com suas séries mesclados com dados da biblioteca.
     * Emite nova lista automaticamente sempre que o banco é alterado.
     * @param workoutId ID do treino.
     */
    fun getExercisesByWorkoutId(workoutId: Long): Flow<List<Exercise>>

    /**
     * Insere um novo exercício no banco.
     * @param exercise ExerciseEntity com workoutId e libraryExerciseId preenchidos.
     * @return Long ID gerado pelo banco.
     */
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    /**
     * Atualiza um exercício existente (ex: mudar o tipo padrão de REPS para DURATION).
     * @param exercise ExerciseEntity com dados atualizados.
     */
    suspend fun updateExercise(exercise: ExerciseEntity)

    /**
     * Deleta um exercício — o CASCADE do ForeignKey apaga as SetEntities automaticamente.
     * @param exercise ExerciseEntity a ser deletado.
     */
    suspend fun deleteExercise(exercise: ExerciseEntity)

    /**
     * Busca um único exercício com suas séries pelo ID, com dados da biblioteca mesclados.
     * @param exerciseId ID do exercício no banco.
     * @return Exercise (domain) ou null se não encontrado.
     */
    suspend fun getExerciseById(exerciseId: Long): Exercise?

    /**
     * Busca a entidade pura sem séries e sem dados da biblioteca.
     * Útil para operações de update/delete que só precisam do objeto persistido.
     * @param exerciseId ID do exercício.
     */
    suspend fun getExerciseEntityById(exerciseId: Long): ExerciseEntity?

    /**
     * Deleta todos os exercícios de um treino (e suas séries via CASCADE).
     * @param workoutId ID do treino.
     */
    suspend fun deleteExercisesByWorkoutId(workoutId: Long)

    /**
     * Conta quantos exercícios um treino possui.
     * @param workoutId ID do treino.
     */
    suspend fun countExercisesByWorkoutId(workoutId: Long): Int
}
