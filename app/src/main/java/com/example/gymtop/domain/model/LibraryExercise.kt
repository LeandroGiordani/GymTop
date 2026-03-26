package com.example.gymtop.domain.model

/**
 * LibraryExercise - Representa um exercício do catálogo (exercise_library.json).
 *
 * IMPORTANTE: Esta classe é SOMENTE LEITURA — nunca é salva no banco de dados.
 * Ela é carregada do JSON uma única vez na inicialização do app e mantida em memória
 * como um Map<String, LibraryExercise> pelo LibraryDataSource (Singleton via Hilt).
 *
 * Por que não salvar no banco?
 * O catálogo de exercícios vem do JSON e é imutável pelo usuário.
 * Duplicar esses dados no banco (para cada exercício de cada treino) desperdiçaria
 * espaço e criaria risco de inconsistência se o JSON for atualizado.
 *
 * Como funciona o fluxo:
 * 1. ExerciseEntity salva apenas o libraryExerciseId (ex: "043")
 * 2. Quando o Repository monta o Exercise (domain), faz lookup no LibraryDataSource
 *    para obter o LibraryExercise correspondente — operação O(1) via HashMap
 * 3. A UI consume Exercise que já carrega o LibraryExercise embutido
 *
 * Padrão MVVM: Pertence ao Domain Layer — é um modelo puro sem dependência de Android.
 * Isso facilita eventual migração para Kotlin Multiplatform (iOS).
 *
 * Campos mapeados do JSON:
 * @param id          campo "id" do JSON (ex: "001", "043", "1156")
 * @param name        campo "nome"
 * @param muscleGroup campo "categoria" (ex: "Costas", "Peito", "Braço")
 * @param muscles     campo "musculos" — lista de músculos envolvidos
 * @param equipment   campo "equipamento" (ex: "Barra", "Halteres", "Cabo")
 * @param thumbnailUrl campo "thumbnail" — caminho relativo do GIF animado
 * @param videoUrl    primeiro item de "male_videos" — URL do vídeo principal
 */
data class LibraryExercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val muscles: List<String>,
    val equipment: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

