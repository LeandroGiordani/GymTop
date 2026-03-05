package com.example.gymtop.domain.model

import com.example.gymtop.data.entity.ExerciseEntity

/**
 * WorkoutEntity - modelo de domínio para um treino.
 *
 * Este arquivo contém apenas o esqueleto mínimo que usamos inicialmente no MVP.
 * - Preferi usar tipos simples (Int para id, String para title) para manter compatibilidade
 *   com as referências já existentes no projeto (ex.: WorkoutListScreen usa Int).
 * - Comentários explicativos foram adicionados para você relembrar conceitos e guiar
 *   futuras extensões (Room entity, validações, timestamps, etc.).
 *
 * TODOs possíveis quando for expandir:
 * - Marcar com @Entity and adicionar Room annotations
 * - Usar Long para id com autogeracao no banco
 * - Adicionar campos como duration, exercises: List<ExerciseEntity>, tags, createdAt
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

