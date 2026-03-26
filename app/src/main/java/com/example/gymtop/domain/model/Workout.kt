package com.example.gymtop.domain.model

/**
 * Workout - Modelo de domínio para um treino.
 *
 * Pertence ao Domain Layer — não tem dependências de Android ou Room.
 * Isso facilita testes unitários e eventual migração para Kotlin Multiplatform.
 *
 * exercises é uma lista de Exercise (domain), onde cada Exercise já carrega
 * seu LibraryExercise (nome, grupo muscular, etc.) e suas séries (List<SetType>).
 * A UI consome este objeto diretamente sem precisar acessar DAOs ou o JSON.
 */

data class Workout(
    // Identificador simples do treino. Para o MVP local usamos Int, mas pode ser Long.
    val id: Long,

    // Título curto do treino (ex: "Peito e Tríceps")
    val title: String,

    // Descrição opcional/observações
    val description: String? = null,

    // Lista de exercícios
    val exercises: List<Exercise> = emptyList()
)

