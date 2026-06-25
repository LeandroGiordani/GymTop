package com.example.gymtop

import app.cash.turbine.test
import com.example.gymtop.domain.model.Workout
import com.example.gymtop.domain.repository.WorkoutRepository
import com.example.gymtop.presentation.viewmodel.UiEvent
import com.example.gymtop.presentation.viewmodel.WorkoutContent
import com.example.gymtop.presentation.viewmodel.WorkoutListViewModel
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val repository = mockk<WorkoutRepository>()
    private lateinit var viewModel: WorkoutListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    private fun createViewModel() = WorkoutListViewModel(repository, dispatcher)

    @Test
    fun `initial content is Loading`() = runTest {
        every { repository.getAllWorkouts() } returns flow { delay(Long.MAX_VALUE) }
        viewModel = createViewModel()

        assertEquals(WorkoutContent.Loading, viewModel.uiState.value.content)
    }

    @Test
    fun `shows Empty when repository returns empty list`() = runTest {
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Loading
            advanceUntilIdle()
            assertEquals(WorkoutContent.Empty, awaitItem().content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows Success when repository returns workouts`() = runTest {
        val workouts = listOf(
            Workout(id = 1L, title = "Peito"),
            Workout(id = 2L, title = "Costas")
        )
        every { repository.getAllWorkouts() } returns flowOf(workouts)
        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Loading
            advanceUntilIdle()
            assertEquals(WorkoutContent.Success(workouts), awaitItem().content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows Error when repository throws`() = runTest {
        every { repository.getAllWorkouts() } returns flow {
            throw RuntimeException("Database error")
        }
        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Loading
            advanceUntilIdle()
            assertEquals(
                WorkoutContent.Error("Erro ao carregar treinos: Database error"),
                awaitItem().content
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `openCreateWorkoutDialog sets showAddWorkoutDialog to true`() = runTest {
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.openCreateWorkoutDialog()

        assertTrue(viewModel.uiState.value.showAddWorkoutDialog)
    }

    @Test
    fun `closeCreateWorkoutDialog sets showAddWorkoutDialog to false`() = runTest {
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.openCreateWorkoutDialog()
        viewModel.closeCreateWorkoutDialog()

        assertFalse(viewModel.uiState.value.showAddWorkoutDialog)
    }

    @Test
    fun `addWorkout calls insertWorkout and closes dialog`() = runTest {
        coEvery { repository.insertWorkout(any()) } just Runs
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.openCreateWorkoutDialog()
        viewModel.addWorkout("Pernas")
        advanceUntilIdle()

        coVerify { repository.insertWorkout("Pernas") }
        assertFalse(viewModel.uiState.value.showAddWorkoutDialog)
    }

    @Test
    fun `addWorkout sends ShowSnackBar event on error`() = runTest {
        coEvery { repository.insertWorkout(any()) } throws RuntimeException("Insert failed")
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.uiEvent.test {
            viewModel.addWorkout("Pernas")
            advanceUntilIdle()
            assertEquals(
                UiEvent.ShowSnackBar("Erro ao adicionar treino: Insert failed"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addWorkout closes dialog even when insert fails`() = runTest {
        coEvery { repository.insertWorkout(any()) } throws RuntimeException("Insert failed")
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.openCreateWorkoutDialog()
        viewModel.addWorkout("Pernas")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showAddWorkoutDialog)
    }

    @Test
    fun `updateWorkout calls repository update`() = runTest {
        val workout = Workout(id = 1L, title = "Costas")
        coEvery { repository.updateWorkout(any()) } just Runs
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.updateWorkout(workout)
        advanceUntilIdle()

        coVerify { repository.updateWorkout(workout) }
    }

    @Test
    fun `updateWorkout sends ShowSnackBar event on error`() = runTest {
        val workout = Workout(id = 1L, title = "Peito")
        coEvery { repository.updateWorkout(any()) } throws RuntimeException("Update failed")
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.uiEvent.test {
            viewModel.updateWorkout(workout)
            advanceUntilIdle()
            assertEquals(
                UiEvent.ShowSnackBar("Erro ao atualizar treino: Update failed"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteWorkout calls repository and sends success snackbar`() = runTest {
        val workout = Workout(id = 1L, title = "Peito")
        coEvery { repository.deleteWorkout(any()) } just Runs
        every { repository.getAllWorkouts() } returns flowOf(listOf(workout))
        viewModel = createViewModel()

        viewModel.uiEvent.test {
            viewModel.deleteWorkout(workout)
            advanceUntilIdle()
            coVerify { repository.deleteWorkout(workout) }
            assertEquals(
                UiEvent.ShowSnackBar("Treino 'Peito' excluído com sucesso"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteWorkout sends error snackbar on failure`() = runTest {
        val workout = Workout(id = 1L, title = "Ombros")
        coEvery { repository.deleteWorkout(any()) } throws RuntimeException("Delete failed")
        every { repository.getAllWorkouts() } returns flowOf(listOf(workout))
        viewModel = createViewModel()

        viewModel.uiEvent.test {
            viewModel.deleteWorkout(workout)
            advanceUntilIdle()
            assertEquals(
                UiEvent.ShowSnackBar("Erro ao excluir treino: Delete failed"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `reloadWorkouts re-observes repository`() = runTest {
        every { repository.getAllWorkouts() } returns flowOf(emptyList())
        viewModel = createViewModel()

        viewModel.reloadWorkouts()
        advanceUntilIdle()

        // Once on init, once on reload
        verify(exactly = 2) { repository.getAllWorkouts() }
    }
}
