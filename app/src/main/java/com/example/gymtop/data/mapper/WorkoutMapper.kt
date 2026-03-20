package com.example.gymtop.data.mapper

import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.entity.ExerciseWithSets
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.entity.WorkoutWithExercises
import com.example.gymtop.data.entity.WorkoutWithExercisesAndSets
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.domain.model.SetType
import com.example.gymtop.domain.model.Workout

/**
 * Converte WorkoutEntity para Workout (domain model)
 * Sem as séries - usa apenas dados básicos do treino
 */
fun WorkoutEntity.toDomain(): Workout =
    Workout(id = id, title = name, description = description)

/**
 * Converte Workout (domain model) para WorkoutEntity
 * Prepara para salvar no banco de dados
 */
fun Workout.toEntity(): WorkoutEntity =
    WorkoutEntity(id = id, name = title, description = description)

/**
 * Converte WorkoutWithExercisesAndSets para Workout (domain model)
 */
fun WorkoutWithExercisesAndSets.toDomain(): Workout =
    Workout(
        id = workoutEntity.id,
        title = workoutEntity.name,
        description = workoutEntity.description,
        exercises = exercisesWithSets.map { it.toDomain() }
    )

/**
 * Converte ExerciseWithSets para Exercise (domain model)
 * Inclui o mapeamento de SetEntity para SetType
 *
 * Fluxo:
 * 1. Ordena as séries pelo setNumber (1, 2, 3...)
 * 2. Para cada série, cria um SetType baseado no tipo (REPS ou DURATION)
 * 3. Retorna Exercise com lista de SetType preenchida
 */
fun ExerciseWithSets.toDomain(): Exercise =
    Exercise(
        id = exerciseEntity.id,
        name = exerciseEntity.name,
        sets = sets
            .sortedBy { it.setNumber }
            .map { setEntity ->
                when (setEntity.type) {
                    "REPS" -> SetType.Reps(
                        count = setEntity.reps ?: 0,
                        weight = setEntity.weight
                    )

                    "DURATION" -> SetType.Duration(
                        seconds = setEntity.duration ?: 0
                    )

                    else -> SetType.Duration(seconds = 0)
                }
            }
    )
