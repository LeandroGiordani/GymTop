package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.domain.repository.WorkoutRepository
import com.example.gymtop.domain.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WorkoutListViewModel - ViewModel que gerencia o estado da UI relacionado a Workouts
 *
 * Padrão MVVM:
 * ViewModel é o intermediário entre a UI e os Dados (Repository).
 * - Mantém o estado da UI em StateFlows (observáveis reativos)
 * - Recebe eventos da UI (usuário clicou em algo)
 * - Interage com Repository para buscar/salvar dados
 * - Emite novo estado quando dados mudam
 *
 * @HiltViewModel: Anotação que permite Hilt injetar este ViewModel automaticamente em composables
 *
 * @Inject: Marca que Repository será injetado via Hilt
 *          Hilt procura em RepositoryModule e encontra provideWorkoutRepository()
 *
 * viewModelScope: Coroutine scope que é cancelado quando o ViewModel é destruído
 *                 Evita memory leaks
 *
 * Padrão MVVM + Kotlin:
 * - StateFlow: Estado observável que emite novos valores (como LiveData mas melhor)
 * - Flow: Stream de dados reativo
 * - suspend fun: Funções assíncronas que precisam de coroutine
 * - launch: Inicia coroutine sem bloquear a thread
 */

data class WorkoutListUiState(
    val content: WorkoutContent = WorkoutContent.Loading,
    val showAddWorkoutDialog: Boolean = false
)

sealed interface WorkoutContent {
    object Loading : WorkoutContent
    object Empty : WorkoutContent
    data class Success(val workouts: List<Workout>) : WorkoutContent
    data class Error(val message: String) : WorkoutContent
}

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState  = MutableStateFlow(WorkoutListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        /**
         * Carrega todos os treinos quando o ViewModel é criado
         * loadAllWorkouts() é chamado automaticamente
         */
        observeWorkouts()
    }

    /**
     * Carrega todos os treinos do Repository e observa mudanças
     * Executa em background (não bloqueia a UI)
     */
    private fun observeWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.getAllWorkouts()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            content = WorkoutContent.Error(message = "Erro ao carregar treinos: ${e.message}")
                        )
                    }
                }
                .collect { workouts ->
                    _uiState.update {
                        it.copy(
                            content = if (workouts.isEmpty()) {
                                WorkoutContent.Empty
                            } else {
                                WorkoutContent.Success(workouts)
                            }
                        )
                    }
                }
        }
    }

    private fun updateDialog(show: Boolean) {
        _uiState.update { it.copy(showAddWorkoutDialog = show) }
    }

    fun openCreateWorkoutDialog() {
        updateDialog(true)
    }

    fun closeCreateWorkoutDialog() {
        updateDialog(false)
    }

    /**
     * Insere um novo treino
     * @param workoutTitle: Treino a ser inserido
     */
    fun addWorkout(workoutTitle: String) {
        viewModelScope.launch {
            try {
                if (workoutTitle.isNotBlank()) {
                    workoutRepository.insertWorkout(workoutTitle)
                }
                // StateFlow será atualizado automaticamente via loadAllWorkouts()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        content = WorkoutContent.Error(message = "Erro ao adicionar treino: ${e.message}")
                    )
                }
            }
            closeCreateWorkoutDialog()
        }
    }

    /**
     * Atualiza um treino existente
     * TODO: Implementar quando UI estiver pronta
     * @param workout: Treino com dados atualizados
     */
    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            try {
                // TODO: Validar dados antes de atualizar
                workoutRepository.updateWorkout(workout)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        content = WorkoutContent.Error(message = "Erro ao atualizar treino: ${e.message}")
                    )
                }
            }
        }
    }

    /**
     * Deleta um treino
     * TODO: Implementar quando UI estiver pronta
     * @param workout: Treino a ser deletado
     */
    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            try {
                workoutRepository.deleteWorkout(workout)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        content = WorkoutContent.Error(message = "Erro ao deletar treino: ${e.message}")
                    )
                }
            }
        }
    }

    fun reloadWorkouts() {
        observeWorkouts()
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário
     * - getWorkoutById(id: Int)
     * - searchWorkouts(query: String)
     * - etc.
     */
}
