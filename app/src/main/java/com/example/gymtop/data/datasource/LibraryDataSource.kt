package com.example.gymtop.data.datasource

import android.content.Context
import com.example.gymtop.R
import com.example.gymtop.domain.model.LibraryExercise
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LibraryDataSource - Fonte de dados do catálogo de exercícios (exercise_library.json).
 *
 * Responsabilidade única: carregar e disponibilizar o catálogo de exercícios do JSON.
 *
 * Por que não é um Repository?
 * Não há operações de escrita nem múltiplas fontes de dados. É apenas uma leitura
 * de um arquivo estático — um DataSource puro é o nível certo de abstração.
 *
 * Estratégia de cache (by lazy):
 * O JSON (~1.2MB, 1156 exercícios) é lido e parseado UMA ÚNICA VEZ na primeira
 * chamada a findById() ou getAll(). O resultado é mantido em memória como um HashMap.
 * Como esta classe é @Singleton (via Hilt), o mapa persiste durante toda a vida do app.
 *
 * Custo: ~1–2MB de RAM, que é negligível em dispositivos Android modernos.
 *
 * TODO (melhoria futura): mover o loadLibrary() para um CoroutineScope de background
 * para evitar bloquear a thread que fizer o primeiro acesso. Por ora, o lazy é aceitável
 * para o MVP pois o app normalmente acessa a biblioteca a partir de uma coroutine.
 *
 * Padrão MVVM: Pertence ao Data Layer. Injetado no ExerciseRepository via Hilt.
 *
 * @param context ApplicationContext injetado pelo Hilt — necessário para abrir res/raw
 */
@Singleton
class LibraryDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Mapa de exercícios indexado pelo id do JSON (ex: "001", "043").
     * Populado na primeira chamada — lazy initialization.
     */
    private val exerciseMap: Map<String, LibraryExercise> by lazy {
        loadLibrary()
    }

    /**
     * Busca um exercício pelo id do JSON.
     * Retorna null se o id não existir no catálogo (não deve ocorrer em uso normal).
     *
     * @param id  id do exercício no JSON, ex: "043"
     */
    fun findById(id: String): LibraryExercise? = exerciseMap[id]

    /**
     * Retorna todos os exercícios do catálogo, sem ordem garantida.
     * Útil para exibir a tela de busca/seleção de exercícios.
     */
    fun getAll(): List<LibraryExercise> = exerciseMap.values.toList()

    // -------------------------------------------------------------------------
    // Parsing interno
    // -------------------------------------------------------------------------

    /**
     * Lê o arquivo res/raw/exercise_library.json e constrói o HashMap.
     *
     * Estrutura esperada no JSON:
     * {
     *   "exercicios": [
     *     {
     *       "id": "001", "nome": "...", "categoria": "...", "musculos": [...],
     *       "equipamento": "...", "thumbnail": "...",
     *       "male_video_side": "...", "male_video_front": "...",
     *       "female_video_side": "...", "female_video_front": "..."
     *     }, ...
     *   ]
     * }
     *
     * Exercícios com apenas um vídeo disponível terão o campo ausente como "".
     */
    private fun loadLibrary(): Map<String, LibraryExercise> {
        val inputStream = context.resources.openRawResource(R.raw.exercise_library)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val root = JSONObject(jsonString)
        val exercisesArray = root.getJSONArray("exercicios")

        return buildMap {
            for (i in 0 until exercisesArray.length()) {
                val obj = exercisesArray.getJSONObject(i)
                val id = obj.getString("id")

                // Converte o JSONArray de músculos para List<String>
                val musclesJson = obj.getJSONArray("musculos")
                val muscles = (0 until musclesJson.length()).map { musclesJson.getString(it) }

                put(
                    id,
                    LibraryExercise(
                        id = id,
                        name = obj.getString("nome"),
                        muscleGroup = obj.getString("categoria"),
                        muscles = muscles,
                        equipment = obj.getString("equipamento"),
                        thumbnailUrl = obj.getString("thumbnail"),
                        // Campos de vídeo nomeados — string vazia se o vídeo não existir
                        maleVideoSide   = obj.optString("male_video_side",   ""),
                        maleVideoFront  = obj.optString("male_video_front",  ""),
                        femaleVideoSide  = obj.optString("female_video_side",  ""),
                        femaleVideoFront = obj.optString("female_video_front", "")
                    )
                )
            }
        }
    }
}
