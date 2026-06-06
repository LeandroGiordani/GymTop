package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymtop.R
import com.example.gymtop.presentation.ui.components.WorkoutListItem
import com.example.gymtop.presentation.viewmodel.WorkoutViewModel
import com.example.gymtop.ui.theme.GymTopBackground
import com.example.gymtop.ui.theme.GymTopNeonGreen
import com.example.gymtop.ui.theme.GymTopTheme

/**
 * WorkoutListScreen — conteúdo da aba Home.
 *
 * Estados possíveis:
 * - Loading: exibe [CircularProgressIndicator] enquanto Room carrega os treinos.
 * - Empty: exibe o estado vazio com ícone e mensagem de boas-vindas.
 * - Content: exibe [LazyColumn] com os treinos cadastrados.
 *
 * O FAB ("Novo treino") vive em [MainScreen] para não ser renderizado aqui —
 * seguindo a recomendação do Scaffold de manter o FAB no nível do Scaffold.
 *
 * @param modifier          Modificador externo — recebe o padding do Scaffold via [MainScreen].
 * @param onNavigateToDetail Callback para navegar ao detalhe de um treino ao clicar no item.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (workoutId: Long) -> Unit = {}
) {
    val viewModel: WorkoutViewModel = hiltViewModel()
    val workouts by viewModel.allWorkouts.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

    Scaffold(
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
        }
    ) { innerPadding ->
        when {
            // ── Loading state ──────────────────────────────────────────────────
            isLoading -> {
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
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Erro desconhecido",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            // ── Empty state ────────────────────────────────────────────────────
            workouts.isEmpty() -> {
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
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(workouts) { workout ->
                        WorkoutListItem(
                            workout = workout,
                            onItemClick = { onNavigateToDetail(workout.id) },
                            onDeleteClick = { viewModel.deleteWorkout(workout) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * WorkoutEmptyState — exibido quando o usuário ainda não criou nenhum treino.
 *
 * Layout:
 * - Ícone de haltere em um círculo escuro com glow decorativo ao redor.
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
