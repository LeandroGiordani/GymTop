package com.example.gymtop.domain.model

data class WorkoutExercise(
    val id: Long,
    val exercise: Exercise,
    val set: List<SetType>,
)
