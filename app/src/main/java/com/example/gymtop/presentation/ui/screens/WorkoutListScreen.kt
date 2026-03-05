package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymtop.presentation.viewmodel.WorkoutViewModel
import com.example.gymtop.presentation.ui.components.WorkoutListItem

/**
 * WorkoutListScreen - Tela que exibe a lista de todos os treinos
 *
 * Padrão Compose:
 * - @Composable: Anotação que marca uma função como composable
 * - Composables são funções que retornam UI - podem ser chamadas recursivamente
 * - Recomposição: Quando estado muda, Compose automaticamente recalcula apenas o que mudou
 *
 * Padrão MVVM em Compose:
 * - ViewModel é obtido via hiltViewModel() (veja exemplo em TODO abaixo)
 * - Estado é observado via ViewModel.stateFlow.collectAsState()
 * - Eventos (usuário clicou) chamam funções do ViewModel
 *
 * @param onNavigateToDetail: Callback quando usuário clica em um treino
 *
 * TODO: Implementar tela completa
 * 1. Obter ViewModel com @HiltViewModel
 * 2. Observar allWorkouts StateFlow
 * 3. Exibir LazyColumn com lista de treinos
 * 4. Adicionar FAB (Floating Action Button) para novo treino
 * 5. Implementar swipe para deletar
 */
@Composable
fun WorkoutListScreen(
    onNavigateToDetail: (workoutId: Long) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("WorkoutEntity List Screen - TODO: Implementar")
    }

    /**
     * EXEMPLO DE IMPLEMENTAÇÃO (comentado):
     *
     * val viewModel: WorkoutViewModel = hiltViewModel()
     * val workouts by viewModel.allWorkouts.collectAsState(initial = emptyList())
     * val isLoading by viewModel.isLoading.collectAsState(initial = false)
     * val errorMessage by viewModel.errorMessage.collectAsState(initial = null)
     *
     * if (errorMessage != null) {
     *     // Mostrar snackbar com erro
     * }
     *
     * if (isLoading) {
     *     CircularProgressIndicator()
     * } else {
     *     LazyColumn {
     *         items(workouts) { workout ->
     *             WorkoutListItem(
     *                 workout = workout,
     *                 onItemClick = { onNavigateToDetail(workout.id) },
     *                 onDeleteClick = { viewModel.deleteWorkout(workout) }
     *             )
     *         }
     *     }
     *     FloatingActionButton(
     *         onClick = { /* navegar para adicionar treino */ }
     *     ) {
     *         Icon(Icons.Default.Add, contentDescription = "Novo treino")
     *     }
     * }
     */
    val viewModel: WorkoutViewModel = hiltViewModel()
    val workouts by viewModel.allWorkouts.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

    if (errorMessage != null) {
        // Exibir texto simples de erro por enquanto; futuramente trocar por Snackbar
        Text(text = errorMessage ?: "Erro desconhecido")
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(workouts) { workout ->
                WorkoutListItem(
                    workout = workout,
                    onItemClick = { onNavigateToDetail(workout.id) },
                    onDeleteClick = { viewModel.deleteWorkout(workout) }
                )
            }
        }
        FloatingActionButton(
            onClick = {  }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Novo treino")
        }
    }
}
