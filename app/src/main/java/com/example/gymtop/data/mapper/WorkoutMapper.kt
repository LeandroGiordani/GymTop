package com.example.gymtop.data.mapper

import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.entity.WorkoutWithExercises
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.domain.model.Workout

fun WorkoutEntity.toDomain(): Workout =
    Workout(id = id, title = name, description = description)

fun Workout.toEntity(): WorkoutEntity =
    WorkoutEntity(id = id, name = title, description = description)

fun ExerciseEntity.toDomain(): Exercise =
    Exercise(
        id = id,
        name = name,
        sets = emptyList()
    )

fun WorkoutWithExercises.toDomain(): Workout =
    Workout(
        id = workoutEntity.id,
        title = workoutEntity.name,
        description = workoutEntity.description,
        exercises = exercises.map { it.toDomain() }
    )