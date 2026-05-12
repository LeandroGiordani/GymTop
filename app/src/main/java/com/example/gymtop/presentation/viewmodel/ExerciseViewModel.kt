package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.domain.repository.ExerciseRepository
import com.example.gymtop.domain.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ExerciseViewModel - ViewModel que gerencia o estado da UI relacionado a Exercises.
 *
 * Padrão MVVM:
 * ViewModel é o intermediário entre a UI e os Dados (Repository).
 * - Mantém o estado da UI em StateFlows (observáveis reativos)
 * - Recebe eventos da UI (usuário clicou em algo)
 * - Interage com Repository para buscar/salvar dados
 * - Emite novo estado quando dados mudam
 *
 * Estado exposto:
 * - exercisesByWorkout: List<Exercise> — já contém LibraryExercise mesclado.
 *   A UI acessa exercise.libraryExercise.name, exercise.libraryExercise.thumbnailUrl, etc.
 *
 * Operações de escrita (insert/update/delete):
 * Ainda recebem ExerciseEntity diretamente, pois o Repository escreve com a entidade.
 * TODO: Quando a UI estiver pronta, criar DTOs/eventos específicos para cada operação.
 *
 * @HiltViewModel: permite injeção automática deste ViewModel em composables via hiltViewModel()
 */
@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    /**
     * Lista de exercícios do treino atual — já com dados da biblioteca mesclados.
     * A UI não precisa acessar o JSON separadamente; tudo está em Exercise.libraryExercise.
     */
    private val _exercisesByWorkout: MutableStateFlow<List<Exercise>> = MutableStateFlow(emptyList())
    val exercisesByWorkout: StateFlow<List<Exercise>> = _exercisesByWorkout

    /**
     * Estado de carregamento
     */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Mensagem de erro
     */
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * ID do treino atualmente selecionado.
     * TODO: Considerar SavedStateHandle para preservar durante rotações de tela.
     */
    private val _currentWorkoutId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentWorkoutId: StateFlow<Long?> = _currentWorkoutId

    /**
     * Carrega e observa os exercícios de um treino específico.
     *
     * O Flow do Repository emite automaticamente quando o banco é alterado,
     * mantendo a lista sempre atualizada sem necessidade de recarregar manualmente.
     *
     * @param workoutId ID do treino (Long — mesmo tipo do banco de dados)
     */
    fun loadExercisesByWorkout(workoutId: Long) {
        _currentWorkoutId.value = workoutId
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // collect nunca termina enquanto o Flow estiver ativo —
                // por isso o isLoading é false apenas após o primeiro emit
                exerciseRepository.getExercisesByWorkoutId(workoutId).collect { exercises ->
                    _exercisesByWorkout.value = exercises
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Erro ao carregar exercícios: ${e.message}"
            }
        }
    }

    /**
     * Insere um novo exercício no banco.
     * Passa ExerciseEntity pois o Repository escreve com a entidade.
     * As séries devem ser inseridas depois via SetRepository.
     *
     * TODO: Substituir por um evento/DTO quando a UI de criação estiver pronta.
     *
     * @param exercise ExerciseEntity com workoutId e libraryExerciseId preenchidos
     */
    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            try {
                exerciseRepository.insertExercise(exercise)
                // _exercisesByWorkout será atualizado automaticamente pelo Flow
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar exercício: ${e.message}"
            }
        }
    }

    /**
     * Atualiza um exercício existente (ex: mudar o tipo padrão de REPS para DURATION).
     * TODO: Implementar quando UI estiver pronta.
     *
     * @param exercise ExerciseEntity com dados atualizados
     */
    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            try {
                exerciseRepository.updateExercise(exercise)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar exercício: ${e.message}"
            }
        }
    }

    /**
     * Deleta um exercício e suas séries (CASCADE no banco).
     * TODO: Implementar quando UI estiver pronta.
     *
     * @param exercise ExerciseEntity a ser deletado
     */
    fun deleteExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            try {
                exerciseRepository.deleteExercise(exercise)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao deletar exercício: ${e.message}"
            }
        }
    }

    /**
     * Limpa a mensagem de erro atual.
     * Chame após exibir o erro na UI para evitar reexibição.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário:
     * - getExerciseById(id: Long): Carregar exercício individual para tela de edição
     * - searchExercises(query: String): Filtrar biblioteca de exercícios
     */
}
