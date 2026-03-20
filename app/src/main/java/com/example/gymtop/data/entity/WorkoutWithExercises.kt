package com.example.gymtop.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercises(
    @Embedded val workoutEntity: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    ) val exercises: List<ExerciseEntity>
)
