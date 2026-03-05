package com.example.gymtop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtop.data.entity.WorkoutEntity
import com.example.gymtop.data.repository.WorkoutRepository
import com.example.gymtop.domain.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WorkoutViewModel - ViewModel que gerencia o estado da UI relacionado a Workouts
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
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    /**
     * Lista de todos os treinos - atualiza automaticamente quando dados mudam no banco
     * Private set garante que apenas ViewModel pode modificar (encapsulamento)
     * _allWorkouts é privado, allWorkouts é público (padrão backing property)
     */
    private val _allWorkouts: MutableStateFlow<List<Workout>> = MutableStateFlow(emptyList())
    val allWorkouts: StateFlow<List<Workout>> = _allWorkouts

    /**
     * Estado de carregamento - para mostrar spinner na UI
     */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Mensagem de erro - para mostrar snackbar ou toast
     */
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        /**
         * Carrega todos os treinos quando o ViewModel é criado
         * loadAllWorkouts() é chamado automaticamente
         */
        loadAllWorkouts()
    }

    /**
     * Carrega todos os treinos do Repository e atualiza _allWorkouts
     * Executa em background (não bloqueia a UI)
     */
    private fun loadAllWorkouts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Coleta o Flow do Repository e atualiza StateFlow
                workoutRepository.getAllWorkouts().collect { workouts ->
                    _allWorkouts.value = workouts
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar treinos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Insere um novo treino
     * TODO: Implementar quando UI estiver pronta
     * @param workout: Treino a ser inserido
     */
    fun addWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            try {
                // TODO: Validar dados antes de inserir
                workoutRepository.insertWorkout(workout)
                // StateFlow será atualizado automaticamente via loadAllWorkouts()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar treino: ${e.message}"
            }
        }
    }

    /**
     * Atualiza um treino existente
     * TODO: Implementar quando UI estiver pronta
     * @param workout: Treino com dados atualizados
     */
    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            try {
                // TODO: Validar dados antes de atualizar
                workoutRepository.updateWorkout(workout)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar treino: ${e.message}"
            }
        }
    }

    /**
     * Deleta um treino
     * TODO: Implementar quando UI estiver pronta
     * @param workout: Treino a ser deletado
     */
    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            try {
                workoutRepository.deleteWorkout(workout)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao deletar treino: ${e.message}"
            }
        }
    }

    /**
     * Limpa mensagem de erro (ex: depois de mostrar ao usuário)
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /**
     * TODO: Implementar métodos adicionais conforme necessário
     * - getWorkoutById(id: Int)
     * - searchWorkouts(query: String)
     * - etc.
     */
}

