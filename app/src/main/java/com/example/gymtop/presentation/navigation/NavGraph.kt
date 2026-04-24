package com.example.gymtop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymtop.presentation.ui.screens.CreatePasswordScreen
import com.example.gymtop.presentation.ui.screens.OnboardingInfoScreen
import com.example.gymtop.presentation.ui.screens.SplashScreen
import com.example.gymtop.presentation.ui.screens.WorkoutDetailScreen
import com.example.gymtop.presentation.ui.screens.WorkoutListScreen
import com.example.gymtop.presentation.viewmodel.OnboardingNavigationEvent
import com.example.gymtop.presentation.viewmodel.OnboardingViewModel
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
    // ── Onboarding / sign-up flow ──────────────────────────────────────────────
    // Duas telas sequenciais: dados pessoais → criação de senha.
    // Compartilham o mesmo OnboardingViewModel (hiltViewModel() no NavGraph).
    object OnboardingInfo   : Screens("onboarding_info")
    object CreatePassword   : Screens("create_password")
    // ── Main app ───────────────────────────────────────────────────────────────
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
        // Rota: Splash / landing
        composable(route = Screens.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        // Usuário retornante — já autenticado, vai direto para o app
                        SplashNavigationEvent.NavigateToWorkoutList -> {
                            navController.navigate(Screens.WorkoutList.route)
                        }
                        // Novo usuário — inicia o fluxo de onboarding (cadastro)
                        SplashNavigationEvent.NavigateToOnboarding -> {
                            navController.navigate(Screens.OnboardingInfo.route)
                        }
                    }
                }
            }

            SplashScreen(
                onStartClick = viewModel::onStartClicked,
                onEnterClick = viewModel::onEnterClicked
            )
        }

        // ── Onboarding flow ────────────────────────────────────────────────────
        // As duas telas compartilham o mesmo ViewModel instanciado no
        // back-stack entry da rota pai "onboarding_info".
        // hiltViewModel() com o mesmo NavBackStackEntry garante que o ViewModel
        // não seja recriado ao navegar entre as duas telas.

        // Rota: tela de informações pessoais (nome, email, gênero)
        composable(route = Screens.OnboardingInfo.route) { entry ->
            val viewModel: OnboardingViewModel = hiltViewModel(entry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        OnboardingNavigationEvent.NavigateToCreatePassword -> {
                            navController.navigate(Screens.CreatePassword.route)
                        }
                        OnboardingNavigationEvent.NavigateToWorkoutList -> {
                            navController.navigate(Screens.WorkoutList.route) {
                                popUpTo(Screens.OnboardingInfo.route) { inclusive = true }
                            }
                        }
                    }
                }
            }

            OnboardingInfoScreen(
                name             = uiState.name,
                email            = uiState.email,
                selectedGender   = uiState.gender,
                errorMessage     = uiState.errorMessage,
                onNameChanged    = viewModel::onNameChanged,
                onEmailChanged   = viewModel::onEmailChanged,
                onGenderSelected = viewModel::onGenderSelected,
                onContinueClicked= viewModel::onInfoContinueClicked
            )
        }

        // Rota: tela de criação de senha
        composable(route = Screens.CreatePassword.route) {
            // Busca o ViewModel do back-stack entry da OnboardingInfoScreen
            // para reutilizar o mesmo state (nome, email, gênero já preenchidos).
            val onboardingEntry = remember(it) {
                navController.getBackStackEntry(Screens.OnboardingInfo.route)
            }
            val viewModel: OnboardingViewModel = hiltViewModel(onboardingEntry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    if (event == OnboardingNavigationEvent.NavigateToWorkoutList) {
                        navController.navigate(Screens.WorkoutList.route) {
                            popUpTo(Screens.OnboardingInfo.route) { inclusive = true }
                        }
                    }
                }
            }

            CreatePasswordScreen(
                password                 = uiState.password,
                confirmPassword          = uiState.confirmPassword,
                isLoading                = uiState.isLoading,
                errorMessage             = uiState.errorMessage,
                onPasswordChanged        = viewModel::onPasswordChanged,
                onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
                onCreateAccountClicked   = viewModel::onCreateAccountClicked
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

