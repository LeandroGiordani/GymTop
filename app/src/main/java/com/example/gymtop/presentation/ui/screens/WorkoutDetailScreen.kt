package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * WorkoutDetailScreen - Tela que exibe detalhes de um treino específico
 *
 * Padrão Compose:
 * - Recebe parâmetros que identificam qual recurso exibir (workoutId)
 * - Observa ViewModel para obter dados atualizados
 * - Permite editar treino e seus exercícios
 *
 * Padrão MVVM em Compose:
 * - ViewModel obtém o treino específico via ID
 * - StateFlows emitem dados quando mudam
 * - UI recompõe automaticamente quando estado muda
 *
 * @param workoutId: ID do treino a exibir
 * @param onNavigateBack: Callback para voltar à tela anterior
 *
 * TODO: Implementar tela completa
 * 1. Obter ViewModel e carregar treino com workoutId
 * 2. Exibir nome, data, descrição do treino
 * 3. Exibir lista de exercícios do treino
 * 4. Adicionar botão para novo exercício
 * 5. Adicionar botão para editar/deletar treino
 */
@Composable
fun WorkoutDetailScreen(
    workoutId: Int,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("WorkoutEntity Detail Screen (ID: $workoutId) - TODO: Implementar")
    }

    /**
     * EXEMPLO DE IMPLEMENTAÇÃO (comentado):
     *
     * val viewModel: WorkoutViewModel = hiltViewModel()
     * val exerciseViewModel: ExerciseViewModel = hiltViewModel()
     *
     * LaunchedEffect(workoutId) {
     *     exerciseViewModel.loadExercisesByWorkout(workoutId)
     * }
     *
     * val workout by viewModel.allWorkouts
     *     .map { workouts -> workouts.firstOrNull { it.id == workoutId } }
     *     .collectAsState(initial = null)
     * val exercises by exerciseViewModel.exercisesByWorkout.collectAsState(initial = emptyList())
     *
     * if (workout != null) {
     *     Column {
     *         TopAppBar(
     *             title = { Text(workout!!.name) },
     *             navigationIcon = {
     *                 IconButton(onClick = onNavigateBack) {
     *                     Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
     *                 }
     *             }
     *         )
     *         Text("Data: ${Date(workout!!.date)}")
     *         Text("Descrição: ${workout!!.description}")
     *
     *         LazyColumn {
     *             items(exercises) { exercise ->
     *                 ExerciseItem(exercise = exercise)
     *             }
     *         }
     *
     *         FloatingActionButton(
     *             onClick = { /* navegar para adicionar exercício */ }
     *         ) {
     *             Icon(Icons.Default.Add, contentDescription = "Novo exercício")
     *         }
     *     }
     * }
     */
}

