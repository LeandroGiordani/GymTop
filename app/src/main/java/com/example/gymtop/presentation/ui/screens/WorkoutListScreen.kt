package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymtop.R
import com.example.gymtop.domain.model.Workout
import com.example.gymtop.presentation.ui.components.WorkoutListItem
import com.example.gymtop.presentation.viewmodel.UiEvent
import com.example.gymtop.presentation.viewmodel.WorkoutContent
import com.example.gymtop.presentation.viewmodel.WorkoutListViewModel
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopOnPrimary
import com.example.gymtop.ui.theme.GymTopTheme

/**
 * WorkoutListScreen — conteúdo da aba Home.
 *
 * Estados possíveis:
 * - Loading: exibe [CircularProgressIndicator] enquanto Room carrega os treinos.
 * - Empty: exibe o estado vazio com ícone e mensagem de boas-vindas.
 * - Content: exibe [LazyColumn] com os treinos cadastrados.
 *
 *
 * @param onNavigateToDetail Callback para navegar ao detalhe de um treino ao clicar no item.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
//    modifier: Modifier = Modifier,
    onNavigateToDetail: (workoutId: Long) -> Unit = {}
) {
    val viewModel: WorkoutListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var editingWorkout by remember { mutableStateOf<Workout?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    editingWorkout?.let { workout ->
        CreateWorkoutDialog(
            dialogLabel = "Editar Treino",
            confirmationButtonText = "Salvar",
            workoutTitle = workout.title,
            onDismiss = { editingWorkout = null },
            onConfirm = { newTitle ->
                viewModel.updateWorkout(workout.copy(title = newTitle))
                editingWorkout = null
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        // ── Top App Bar ───────────────────────────────────────────────
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GymTopBackground,
                    titleContentColor = GymTopNeonGreen
                ),
                title = {
                    Text(
                        text = "GYM TOP",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /* open edit profile options */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_profile_icon),
                            contentDescription = "Icone de Perfil",
                            tint = GymTopNeonGreen
                        )
                    }
                }
            )
        },

        // ── Floating Action Button — only on Home tab ──────────────────────────
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::openCreateWorkoutDialog,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Novo treino"
                )
            }
        },

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (val state = uiState.content) {

            // ── Loading state ──────────────────────────────────────────────────
            is WorkoutContent.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ── Error state ────────────────────────────────────────────────────
            is WorkoutContent.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        color = MaterialTheme.colorScheme.error,
                    )

                    Button(
                        onClick = viewModel::reloadWorkouts,
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GymTopNeonGreen,
                            contentColor   = GymTopOnPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ) {
                        Text(
                            text          = "Tentar Novamente",
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 2.8.sp
                        )
                    }
                }
            }

            // ── Empty state ────────────────────────────────────────────────────
            is WorkoutContent.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    WorkoutEmptyState()
                }
            }

            // ── Content state ──────────────────────────────────────────────────
            is WorkoutContent.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = state.workouts,
                        key = { it.id }
                    ) { workout ->
                        WorkoutListItem(
                            workout = workout,
                            onTitleClick = { editingWorkout = workout },
                            onItemClick = { onNavigateToDetail(workout.id) },
                            onDeleteClick = { viewModel.deleteWorkout(workout) }
                        )
                    }
                }
            }
        }

        if (uiState.showAddWorkoutDialog) {
            CreateWorkoutDialog(
                dialogLabel = "Novo Treino",
                confirmationButtonText = "Criar",
                onDismiss = viewModel::closeCreateWorkoutDialog,
                onConfirm = { workoutTitle ->
                    viewModel.addWorkout(workoutTitle)
                    viewModel.closeCreateWorkoutDialog()
                }
            )
        }
    }
}

/**
 * WorkoutEmptyState — exibido quando o usuário ainda não criou nenhum treino.
 *
 * Layout:
 * - Ícone de haltere num círculo escuro com glow decorativo ao redor.
 * - Mensagem motivacional abaixo.
 *
 * Design inspirado no mockup: círculo [GymTopSurface] com ícone [GymTopNeonGreen],
 * circundado por um halo desfocado para simular um brilho ambiente.
 */
@Composable
private fun WorkoutEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Glow + icon stack ──────────────────────────────────────────────────
        Box(contentAlignment = Alignment.Center) {
            // Decorative ambient glow behind the circle
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        color = GymTopNeonGreen.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(radius = 24.dp)
            )

            // Dark circle container
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    tint = GymTopNeonGreen,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Motivational message ───────────────────────────────────────────────
        Text(
            text = "Sua jornada começa aqui. Crie seu primeiro treino para começar a monitorar sua evolução.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutEmptyStatePreview() {
    GymTopTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            WorkoutEmptyState()
        }
    }
}
