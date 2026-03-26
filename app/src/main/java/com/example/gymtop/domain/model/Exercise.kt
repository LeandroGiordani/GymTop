package com.example.gymtop.domain.model

/**
 * Exercise - Modelo de domínio que representa um exercício dentro de um treino.
 *
 * Este é o objeto "montado" que a UI consome. Ele combina duas fontes de dados:
 *
 *   1. ExerciseEntity (banco de dados Room)
 *      → fornece: id, workoutId, type (não exposto diretamente aqui)
 *
 *   2. LibraryExercise (exercise_library.json via LibraryDataSource)
 *      → fornece: name, muscleGroup, muscles, equipment, thumbnailUrl, videoUrl
 *
 *   3. List<SetEntity> → mapeados para List<SetType> (via WorkoutMapper)
 *      → fornece: as séries do exercício configuradas pelo usuário
 *
 * Essa combinação acontece no ExerciseRepository antes de entregar para a ViewModel.
 * A UI nunca precisa saber de onde veio cada dado — só consome Exercise.
 *
 * Para acessar o nome do exercício: exercise.libraryExercise.name
 * Para acessar o grupo muscular:    exercise.libraryExercise.muscleGroup
 * Para acessar as séries:           exercise.sets
 *
 * Padrão MVVM: Pertence ao Domain Layer — modelo puro sem dependências de Android.
 *
 * @param id             PK gerado pelo Room (ExerciseEntity.id)
 * @param workoutId      FK do treino ao qual pertence
 * @param libraryExercise dados do catálogo (nome, grupo muscular, equipamento, etc.)
 * @param sets           séries configuradas pelo usuário, já mapeadas de SetEntity
 */
data class Exercise(
    val id: Long,
    val workoutId: Long,
    val libraryExercise: LibraryExercise,
    val sets: List<SetType>
)
