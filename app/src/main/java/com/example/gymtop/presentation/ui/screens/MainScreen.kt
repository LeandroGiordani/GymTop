package com.example.gymtop.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtop.ui.theme.GymTopTheme

/**
 * BottomNavItem — representa cada destino da barra de navegação inferior.
 *
 * @param label           Texto exibido abaixo do ícone.
 * @param selectedIcon    Ícone exibido quando o item está selecionado (filled).
 * @param unselectedIcon  Ícone exibido quando o item não está selecionado (outlined).
 */
private data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * Destinos disponíveis na bottom navigation bar.
 * Progress será adicionado em sprint futuro.
 */
private val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        label = "Biblioteca",
        selectedIcon = Icons.Filled.FitnessCenter,
        unselectedIcon = Icons.Outlined.FitnessCenter
    )
)

/**
 * MainScreen — shell principal do app pós-login.
 *
 * Responsabilidades:
 * - Exibe a [NavigationBar] com as abas Home e Biblioteca.
 * - Gerencia qual aba está ativa via [rememberSaveable] (sobrevive a rotação).
 * - Exibe o FAB (Floating Action Button) apenas na aba Home para criar novo treino.
 * - Delega o conteúdo de cada aba para os composables filhos.
 *
 * @param onNavigateToDetail Callback para navegar para o detalhe de um treino (aba Home).
 * @param onAddWorkout       Callback para abrir a tela de criação de treino.
 */
@Composable
fun MainScreen(
    onNavigateToDetail: (workoutId: Long) -> Unit = {},
    onAddWorkout: () -> Unit = {}
) {
    // rememberSaveable persiste o índice selecionado em rotações de tela
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        // ── Bottom Navigation Bar ──────────────────────────────────────────────
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTabIndex == index) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.label
                            )
                        },
                        label = { Text(text = item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            // Selected state uses the brand neon green
                            selectedIconColor   = MaterialTheme.colorScheme.primary,
                            selectedTextColor   = MaterialTheme.colorScheme.primary,
                            // Indicator behind selected icon — use surface to keep it subtle
                            indicatorColor      = MaterialTheme.colorScheme.surface,
                            // Unselected state uses a dimmer color for contrast
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        },

        // ── Floating Action Button — only on Home tab ──────────────────────────
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = onAddWorkout,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Novo treino"
                    )
                }
            }
        },

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        // ── Tab content ────────────────────────────────────────────────────────
        when (selectedTabIndex) {
            0 -> WorkoutListScreen(
                modifier = Modifier.padding(innerPadding),
                onNavigateToDetail = onNavigateToDetail
            )
            1 -> LibraryScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    GymTopTheme {
        MainScreen()
    }
}
