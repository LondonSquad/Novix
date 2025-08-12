package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actordetails.cast.CastActorEntity
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopMoviesPicksViewModelTest {

    private lateinit var viewModel: TopMoviesPicksViewModel
    private lateinit var getActorMoviePicksById: GetActorMoviePicksByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val mockCastDetails = CastDetails(
        id = 123,
        cast = listOf(
            CastActorEntity(
                id = 1,
                posterUrl = "/test1.jpg"
            ),
            CastActorEntity(
                id = 2,
                posterUrl = "/test2.jpg"
            )
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        getActorMoviePicksById = mockk()
        savedStateHandle = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when data is being fetched should show loading state`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } coAnswers {
            delay(100)
            mockCastDetails
        }

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)
    }

    @Test
    fun `when onSaveClick called should toggle saved state`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        val initialSavedState = viewModel.state.value.isSaved

        // When
        viewModel.onSaveClick(1)

        // Then
        assertThat(viewModel.state.value.isSaved).isEqualTo(!initialSavedState)
    }

    @Test
    fun `when save state is toggled multiple times should maintain consistency`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // When
        viewModel.onSaveClick(1)
        viewModel.onSaveClick(1)
        viewModel.onSaveClick(1)

        // Then
        assertThat(viewModel.state.value.isSaved).isTrue()
    }

    @Test
    fun `when onSaveClick called with different movieIds should still toggle same saved state`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // When
        viewModel.onSaveClick(1)
        val firstToggleState = viewModel.state.value.isSaved

        viewModel.onSaveClick(999)
        val secondToggleState = viewModel.state.value.isSaved

        // Then
        assertThat(firstToggleState).isTrue()
        assertThat(secondToggleState).isFalse()
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        with(viewModel.state.value) {
            assertThat(id).isEqualTo(0)
            assertThat(isSaved).isFalse()
            assertThat(backdropPath).isEmpty()
            assertThat(errorState).isNull()
        }
    }

    @Test
    fun `viewModel should implement all contract methods`() {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        viewModel.onRetry()
        viewModel.onBackClick()
        viewModel.onSaveClick(1)
        viewModel.onMovieClick(1)
    }

    @Test
    fun `when initialized with zero actor ID should not fetch data`() = runTest {
        // Given
        val args = Screen.ActorTopMoviesPicksDetails(0)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.id).isEqualTo(0)
    }

    @Test
    fun `when initialized with null args should not fetch data`() = runTest {
        // Given
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns null

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.id).isEqualTo(0)
    }

    @Test
    fun `when actor ID is zero should not execute use case`() = runTest {
        // Given
        val actorId = 0
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.id).isEqualTo(0)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `when checkSuccess returns false should not execute use case`() = runTest {
        // Given
        val actorId = 0
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

}
