package com.example.gymtop.domain.model

sealed class SetType {
    data class Reps(val count: Int, val weight: Int?) : SetType()
    data class Duration(val seconds: Long) : SetType()
}
