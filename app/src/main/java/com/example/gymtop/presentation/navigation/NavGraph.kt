package com.example.gymtop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymtop.presentation.ui.screens.SplashScreen
import com.example.gymtop.presentation.ui.screens.WorkoutDetailScreen
import com.example.gymtop.presentation.ui.screens.WorkoutListScreen
import com.example.gymtop.presentation.viewmodel.SplashNavigationEvent
import com.example.gymtop.presentation.viewmodel.SplashViewModel

/**
 * NavGraph - Define a estrutura de navegação do app
 *
 * Navigation Compose permite navegar entre telas de forma declarativa (como todo Compose).
 *
 * Conceitos:
 * - NavController: Controla a navegação entre composables
 * - NavHost: Container que exibe a tela atual baseado na rota
 * - Rotas: Strings que identificam telas (ex: "workout_list", "workout_detail")
 * - Arguments: Parâmetros passados entre rotas (ex: workoutId)
 *
 * Padrão Compose Navigation:
 * 1. Definir rotas como objetos (melhor que strings hardcoded)
 * 2. NavHost define qual composable exibir para cada rota
 * 3. navigate() é chamado para mudar de tela
 * 4. Arguments são passados via URI (ex: "workout_detail/{workoutId}")
 */

/**
 * Rotas do app - cada tela tem uma rota única
 * Objeto selado garante type safety
 */
sealed class Screens(val route: String) {
    object Splash : Screens("splash")
    object WorkoutList : Screens("workout_list")
    object WorkoutDetail : Screens("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout_detail/$workoutId"
    }
    // TODO: Adicionar mais telas conforme necessário
    // object AddWorkout : Screens("add_workout")
    // object ExerciseList : Screens("exercise_list/{workoutId}")
}

/**
 * NavGraph - Composable que configura toda a navegação
 *
 * @param navController: Controlador de navegação (gerencia a stack de telas)
 *
 * NavHost:
 * - startDestination: Tela inicial do app (WorkoutListScreen)
 * - builder: Lambda que define as rotas
 *
 * composable():
 * - route: String que identifica a rota
 * - content: Lambda que retorna o Composable a ser exibido
 * - arguments: (opcional) Define argumentos da rota (tipo, valor padrão)
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        // Rota: Splash / onboarding
        composable(route = Screens.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()

            LaunchedEffect(navigationEvent) {
                when (navigationEvent) {
                    SplashNavigationEvent.NavigateToWorkoutList -> {
                        navController.navigate(Screens.WorkoutList.route) {
                            popUpTo(Screens.Splash.route) { inclusive = true }
                        }
                        viewModel.onNavigationEventConsumed()
                    }
                    null -> Unit
                }
            }

            SplashScreen(
                onStartClick = viewModel::onStartClicked,
                onEnterClick = viewModel::onEnterClicked
            )
        }

        // Rota: Tela de lista de treinos
        composable(route = Screens.WorkoutList.route) {
            WorkoutListScreen(
                onNavigateToDetail = { workoutId ->
                    navController.navigate(Screens.WorkoutDetail.createRoute(workoutId))
                }
            )
        }

        // Rota: Tela de detalhes de um treino
        // {workoutId} é um placeholder que será preenchido com o ID real
        composable(route = Screens.WorkoutDetail.route) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull() ?: return@composable
            WorkoutDetailScreen(
                workoutId = workoutId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * TODO: Adicionar mais rotas conforme necessário
         *
         * composable(route = Screens.AddWorkout.route) {
         *     AddWorkoutScreen(
         *         onNavigateBack = { navController.popBackStack() }
         *     )
         * }
         *
         * composable(
         *     route = Screens.ExerciseList.route,
         *     arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
         * ) { backStackEntry ->
         *     val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: return@composable
         *     ExerciseListScreen(
         *         workoutId = workoutId,
         *         onNavigateBack = { navController.popBackStack() }
         *     )
         * }
         */
    }
}

