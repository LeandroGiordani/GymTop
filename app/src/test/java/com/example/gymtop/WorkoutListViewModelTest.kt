package com.example.gymtop

import app.cash.turbine.test
import com.example.gymtop.domain.model.Workout
import com.example.gymtop.domain.repository.WorkoutRepository
import com.example.gymtop.presentation.viewmodel.WorkoutContent
import com.example.gymtop.presentation.viewmodel.WorkoutListViewModel
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(
    ExperimentalCoroutinesApi::class
)
class WorkoutListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val repository = mockk<WorkoutRepository>()

    private lateinit var viewModel: WorkoutListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(
            dispatcher
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `shows error state when repository returns error`() =
        runTest {
            every {
                repository.getAllWorkouts()
            } returns flow { throw RuntimeException("Database error") }

            viewModel = WorkoutListViewModel(repository)
            advanceUntilIdle()

            viewModel.uiState.test {
                assertEquals(
                    WorkoutContent.Error("Erro ao carregar treinos: Database error"),
                    awaitItem().content
                )
            }
        }

    @Test
    fun `shows empty state when repository returns empty list`() =
        runTest {
            every {
                repository.getAllWorkouts()
            } returns flowOf(
                emptyList()
            )

            viewModel = WorkoutListViewModel(repository)

            viewModel.uiState.test {
                assertEquals(
                    WorkoutContent.Empty,
                    awaitItem().content
                )
            }
        }

    @Test
    fun `shows workouts when repository returns items`() =
        runTest {
            val workouts =
                listOf(
                    Workout(id = 1L, title = "Push"),
                    Workout(id = 2L, title ="Pull")
                )

            every {
                repository.getAllWorkouts()
            } returns flowOf(
                workouts
            )

            viewModel = WorkoutListViewModel(repository)

            viewModel.uiState.test {
                assertEquals(
                    WorkoutContent.Success(
                        workouts
                    ),
                    awaitItem().content
                )
            }
        }

    @Test
    fun `openCreateDialog updates state`() =
        runTest {
            every {
                repository.getAllWorkouts()
            } returns flowOf(
                emptyList()
            )

            viewModel =
                WorkoutListViewModel(
                    repository
                )

            viewModel.openCreateWorkoutDialog()

            assertTrue(
                viewModel
                    .uiState
                    .value
                    .showAddWorkoutDialog
            )
        }

    @Test
    fun `closeCreateDialog updates state`() =
        runTest {
            every {
                repository.getAllWorkouts()
            } returns flowOf(
                emptyList()
            )

            viewModel =
                WorkoutListViewModel(
                    repository
                )

            viewModel.openCreateWorkoutDialog()

            viewModel.closeCreateWorkoutDialog()

            assertFalse(
                viewModel
                    .uiState
                    .value
                    .showAddWorkoutDialog
            )
        }

    @Test
    fun `createWorkout creates workout and closes dialog`() =
        runTest {
            coEvery {
                repository.insertWorkout(
                    any()
                )
            } just Runs

            every {
                repository.getAllWorkouts()
            } returns flowOf(
                emptyList()
            )

            viewModel =
                WorkoutListViewModel(
                    repository
                )

            viewModel.openCreateWorkoutDialog()

            viewModel.addWorkout(
                "Leg Day"
            )

            advanceUntilIdle()

            coVerify {
                repository.insertWorkout(
                    "Leg Day"
                )
            }

            assertFalse(
                viewModel
                    .uiState
                    .value
                    .showAddWorkoutDialog
            )
        }
}
