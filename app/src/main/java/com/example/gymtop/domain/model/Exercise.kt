package com.example.gymtop.domain.model

data class Exercise(
    val id: Long,
    val name: String,
    val sets: List<ExerciseSet>
)
