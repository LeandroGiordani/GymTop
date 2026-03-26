package com.example.gymtop.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * ExerciseEntity - Entidade Room que representa um exercício dentro de um treino.
 *
 * DESIGN DECISION — por que esta entidade é "lean" (enxuta)?
 * O nome, grupo muscular, equipamento, vídeos e thumbnail NÃO são armazenados aqui.
 * Esses dados vêm do catálogo (exercise_library.json) e são acessados via LibraryDataSource.
 * Armazená-los aqui criaria duplicação: se o usuário tem 20 treinos com "Supino", o nome
 * "Supino" estaria 20 vezes no banco — redundante e inconsistente se o JSON mudar.
 *
 * O que esta entidade armazena:
 * - libraryExerciseId: referência ao exercício no catálogo JSON (ex: "043")
 * - type: tipo padrão das séries ("REPS" ou "DURATION"), escolhido pelo usuário
 *
 * As séries em si ficam na tabela SetEntity (relação 1:N via exerciseId).
 * O nome e demais info do exercício são buscados no LibraryDataSource (Map em memória).
 *
 * @ForeignKey CASCADE: deletar um treino apaga todos seus exercícios automaticamente.
 * @Relation em ExerciseWithSets une ExerciseEntity + List<SetEntity> em uma única query.
 *
 * Padrão MVVM: Pertence ao Data Layer — representa apenas estrutura de persistência.
 */
@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Foreign Key — qual treino este exercício pertence
    val workoutId: Long,

    // Referência ao catálogo JSON — NÃO duplicamos nome/equipamento/etc aqui
    // O ExerciseRepository faz lookup no LibraryDataSource usando este id
    val libraryExerciseId: String,

    // Tipo padrão das séries deste exercício, definido pelo usuário ao montar o treino
    // "REPS"     → séries por repetições (ex: 3x10)
    // "DURATION" → séries por tempo (ex: 3x30seg)
    val type: String
)
