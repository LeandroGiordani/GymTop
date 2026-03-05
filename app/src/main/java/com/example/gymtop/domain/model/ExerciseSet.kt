package com.example.gymtop.domain.model

sealed class ExerciseSet {
    data class Reps(val count: Int, val weight: Double?) : ExerciseSet()
    data class Duration(val seconds: Int) : ExerciseSet()
}
