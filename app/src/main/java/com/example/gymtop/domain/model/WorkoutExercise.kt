package com.example.gymtop.domain.model

data class WorkoutExercise(
    val id: Long,
    val exercise: Exercise,
    val sets: Int,
    val exerciseSet: List<SetType>,
    val weight: Double?
)
