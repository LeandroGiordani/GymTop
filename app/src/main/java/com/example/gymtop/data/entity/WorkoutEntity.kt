package com.example.gymtop.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * WorkoutEntity - Entidade Room que representa um treino
 *
 * @Entity: Anotação que marca esta classe como uma tabela no banco de dados Room.
 * Room gera queries SQL automaticamente baseado nesta classe.
 *
 * Atributos:
 * - id: Identificador único do treino (PrimaryKey), gerado automaticamente
 * - name: Nome do treino (ex: "Perna", "Costas")
 * - date: Data em que o treino foi realizado (timestamp em milissegundos)
 * - description: Descrição opcional do treino
 *
 * Padrão MVVM: Esta entidade está no Data Layer - representa a estrutura bruta dos dados
 * no banco de dados, sem lógica de negócio.
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val description: String? = null,
)

