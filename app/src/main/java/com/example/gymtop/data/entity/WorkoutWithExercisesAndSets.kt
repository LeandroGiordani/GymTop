// arquivo: app/src/main/java/com/example/gymtop/data/entity/WorkoutWithExercisesAndSets.kt
package com.example.gymtop.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * WorkoutWithExercisesAndSets - Relação que inclui um treino,
 * seus exercícios, E todas as séries de cada exercício.
 *
 * Hierarquia:
 * WorkoutEntity (1) → (many) ExerciseEntity (1) → (many) SetEntity
 */
data class WorkoutWithExercisesAndSets(
    @Embedded val workoutEntity: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = ExerciseEntity::class
    )
    val exercisesWithSets: List<ExerciseWithSets>
)
