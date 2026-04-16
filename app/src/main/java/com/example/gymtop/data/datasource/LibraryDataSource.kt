package com.example.gymtop.data.datasource

import android.content.Context
import com.example.gymtop.R
import com.example.gymtop.di.ApplicationScope
import com.example.gymtop.domain.model.LibraryExercise
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
 * Estratégia de carregamento (Deferred / async):
 * O JSON (~1.2MB) é lido e parseado em background (Dispatchers.IO) assim que este
 * Singleton é criado pelo Hilt. O resultado fica disponível via [exerciseMapDeferred].
 *
 * Vantagem sobre "by lazy":
 * Com "by lazy", o parsing ocorria na thread que fazia o primeiro acesso — potencialmente
 * a Main Thread. Com Deferred + async(IO), o parsing SEMPRE ocorre em uma thread de I/O,
 * e qualquer chamante apenas suspende (sem bloquear) enquanto aguarda o resultado.
 *
 * Custo: ~1–2MB de RAM, que é negligível em dispositivos Android modernos.
 *
 * Padrão MVVM: Pertence ao Data Layer. Injetado no ExerciseRepository via Hilt.
 *
 * @param context  ApplicationContext injetado pelo Hilt — necessário para abrir res/raw
 * @param appScope CoroutineScope de nível de aplicação injetado pelo Hilt via
 *                 [com.example.gymtop.di.CoroutineScopeModule]. Garante que o carregamento
 *                 do JSON não está atrelado ao ciclo de vida de nenhuma tela.
 */
@Singleton
class LibraryDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:ApplicationScope private val appScope: CoroutineScope
) {

    /**
     * Carregamento assíncrono iniciado imediatamente na criação do Singleton.
     *
     * async(Dispatchers.IO): garante que o I/O de disco e o parsing do JSON ocorrem
     * em uma thread de I/O, nunca na Main Thread.
     *
     * Deferred<T>: funciona como um "Future" — o resultado fica disponível quando
     * o async terminar. Chamadas a [findById] e [getAll] suspendem até que esteja pronto.
     *
     * SupervisorJob no appScope: se o carregamento falhar, o erro não se propaga para
     * outros filhos do scope (isolamento de falhas).
     */
    private val exerciseMapDeferred: Deferred<Map<String, LibraryExercise>> =
        appScope.async(Dispatchers.IO) { loadLibrary() }

    /**
     * Busca um exercício pelo id do JSON.
     *
     * Suspende enquanto o carregamento inicial estiver em andamento (apenas na primeira
     * chamada, e somente se o async ainda não terminou). Nas chamadas subsequentes,
     * [exerciseMapDeferred] já está resolvido e await() retorna instantaneamente.
     *
     * Retorna null se o id não existir no catálogo (não deve ocorrer em uso normal).
     *
     * @param id id do exercício no JSON, ex: "043"
     */
    suspend fun findById(id: String): LibraryExercise? = exerciseMapDeferred.await()[id]

    /**
     * Retorna todos os exercícios do catálogo, sem ordem garantida.
     *
     * Assim como [findById], suspende apenas se o carregamento ainda não terminou.
     * Útil para exibir a tela de busca/seleção de exercícios.
     */
    suspend fun getAll(): List<LibraryExercise> = exerciseMapDeferred.await().values.toList()

    // -------------------------------------------------------------------------
    // Parsing interno
    // -------------------------------------------------------------------------

    /**
     * Lê o arquivo res/raw/exercise_library.json e constrói o HashMap.
     * Executado em Dispatchers.IO via [exerciseMapDeferred].
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
