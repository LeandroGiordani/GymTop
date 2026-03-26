package com.example.gymtop.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * ExerciseEntity - Entidade Room que representa um exercício dentro de um treino
 *
 * @Entity: Marca como tabela no banco de dados
 * @ForeignKey: Define a relação com WorkoutEntity - cada exercício pertence a um treino
 *   - onDelete = ForeignKey.CASCADE: Se um treino for deletado, seus exercícios também serão
 *
 * Atributos:
 * - id: Identificador único do exercício
 * - name: Nome do exercício (ex: "Supino", "Agachamento")
 * - sets: Número de séries
 * - reps: Número de repetições por série
 * - weight: Peso usado (em kg)
 * - workoutId: Foreign Key - referencia o treino ao qual este exercício pertence
 *
 * Padrão MVVM: Entidade no Data Layer, representa estrutura bruta do exercício no BD
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

    val workoutId: Long,

    val name: String,

    val librarySlug
)

