package com.example.gymtop.data.mapper

import android.util.Log
import com.example.gymtop.data.entity.ExerciseWithSets
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.entity.WorkoutWithExercisesAndSets
import com.example.gymtop.domain.model.Exercise
import com.example.gymtop.domain.model.LibraryExercise
import com.example.gymtop.domain.model.SetType
import com.example.gymtop.domain.model.SetTypeValue
import com.example.gymtop.domain.model.Workout

/**
 * Converte WorkoutEntity para Workout (domain model) — sem exercícios.
 * Usado quando só precisamos dos dados básicos do treino (ex: lista de treinos).
 */
fun WorkoutEntity.toDomain(): Workout =
    Workout(id = id, title = name, description = description)

/**
 * Converte Workout (domain model) para WorkoutEntity — para persistir no banco.
 */
fun Workout.toEntity(): WorkoutEntity =
    WorkoutEntity(id = id, name = title, description = description)

/**
 * Converte WorkoutWithExercisesAndSets para Workout (domain model) — com exercícios.
 *
 * Recebe o mapa da biblioteca para resolver cada exercício.
 *
 * @param libraryMap Map<id, LibraryExercise> — provido pelo LibraryDataSource.
 *                   Exercícios sem id correspondente no mapa são silenciosamente ignorados.
 */
fun WorkoutWithExercisesAndSets.toDomain(
    libraryMap: Map<String, LibraryExercise>
): Workout =
    Workout(
        id = workoutEntity.id,
        title = workoutEntity.name,
        description = workoutEntity.description,
        exercises = exercisesWithSets.mapNotNull { exerciseWithSets ->
            val lib = libraryMap[exerciseWithSets.exerciseEntity.libraryExerciseId]
                ?: run {
                    Log.w(
                        "WorkoutMapper",
                        "libraryExerciseId '${exerciseWithSets.exerciseEntity.libraryExerciseId}' not found in catalog — exercise omitted"
                    )
                    return@mapNotNull null  // ignora se id não existe no catálogo
                }
            exerciseWithSets.toDomain(lib)
        }
    )

/**
 * Converte ExerciseWithSets para Exercise (domain model).
 *
 * Por que recebe libraryExercise como parâmetro?
 * ExerciseEntity não armazena nome/equipamento/etc (design "lean"). Esses dados
 * ficam no JSON e são resolvidos pelo Repository antes de chamar este mapper.
 * O mapper em si não sabe de onde o LibraryExercise veio — só faz a conversão.
 *
 * Fluxo das séries:
 * 1. Ordena SetEntity pelo setNumber (ordem original do usuário)
 * 2. Para cada SetEntity, cria SetType.Reps ou SetType.Duration conforme o campo type
 *
 * @param libraryExercise dados do catálogo correspondente ao exercício
 */
fun ExerciseWithSets.toDomain(libraryExercise: LibraryExercise): Exercise =
    Exercise(
        id = exerciseEntity.id,
        workoutId = exerciseEntity.workoutId,
        libraryExercise = libraryExercise,
        sets = sets
            .sortedBy { it.setNumber }
            .map { setEntity ->
                when (setEntity.type) {
                    SetTypeValue.REPS -> SetType.Reps(
                        count = setEntity.reps ?: 0,
                        weight = setEntity.weight
                    )
                    SetTypeValue.DURATION -> SetType.Duration(
                        seconds = setEntity.duration ?: 0
                    )
                }
            }
    )
