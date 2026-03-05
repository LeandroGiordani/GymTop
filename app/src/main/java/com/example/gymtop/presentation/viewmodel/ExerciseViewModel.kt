package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.data.entity.ExerciseEntity
import com.example.gymtop.data.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ExerciseViewModel - ViewModel que gerencia o estado da UI relacionado a Exercises
 *
 * Padrão MVVM:
 * ViewModel é o intermediário entre a UI e os Dados (Repository).
 * - Mantém o estado da UI em StateFlows (observáveis reativos)
 * - Recebe eventos da UI
 * - Interage com Repository para buscar/salvar dados
 * - Emite novo estado quando dados mudam
 *
 * @HiltViewModel: Permite Hilt injetar este ViewModel em composables automaticamente
 *
 * @Inject: Repository será injetado via Hilt
 *
 * Nota: Este ViewModel gerencia exercícios de um treino específico
 * Um exercício sempre pertence a um treino (através de workoutId)
 */
@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    /**
     * Lista de exercícios do treino atual
     * Observável que emite novos valores quando há mudanças
     */
    private val _exercisesByWorkout: MutableStateFlow<List<ExerciseEntity>> = MutableStateFlow(emptyList())
    val exercisesByWorkout: StateFlow<List<ExerciseEntity>> = _exercisesByWorkout

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
     * ID do treino atualmente selecionado
     * TODO: Pode ser necessário usar SavedStateHandle para preservar entre rotações
     */
    private val _currentWorkoutId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentWorkoutId: StateFlow<Int?> = _currentWorkoutId

    /**
     * Carrega exercícios de um treino específico
     * @param workoutId: ID do treino cujos exercícios queremos carregar
     */
    fun loadExercisesByWorkout(workoutId: Int) {
        _currentWorkoutId.value = workoutId
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Coleta o Flow do Repository e atualiza StateFlow
                exerciseRepository.getExercisesByWorkoutId(workoutId).collect { exercises ->
                    _exercisesByWorkout.value = exercises
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar exercícios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Insere um novo exercício
     * TODO: Implementar quando UI estiver pronta
     * @param exercise: Exercício a ser inserido
     */
    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            try {
                // TODO: Validar dados antes de inserir
                // - name não vazio
                // - sets > 0
                // - reps > 0
                exerciseRepository.insertExercise(exercise)
                // StateFlow será atualizado automaticamente
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar exercício: ${e.message}"
            }
        }
    }

    /**
     * Atualiza um exercício existente
     * TODO: Implementar quando UI estiver pronta
     * @param exercise: Exercício com dados atualizados
     */
    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            try {
                // TODO: Validar dados
                exerciseRepository.updateExercise(exercise)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar exercício: ${e.message}"
            }
        }
    }

    /**
     * Deleta um exercício
     * TODO: Implementar quando UI estiver pronta
     * @param exercise: Exercício a ser deletado
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
     * Limpa mensagem de erro
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário
     * - getExerciseById(id: Int)
     * - searchExercises(query: String)
     * - getExerciseStats(exerciseId: Int)
     * etc.
     */
}

