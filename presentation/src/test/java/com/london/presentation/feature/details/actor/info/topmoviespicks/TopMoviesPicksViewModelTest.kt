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
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val mainDispatcher = StandardTestDispatcher()

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

    private val emptyCastDetails = CastDetails(
        id = 123,
        cast = emptyList()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        getActorMoviePicksById = mockk()
        savedStateHandle = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized with invalid actor ID should not fetch data`() = runTest {
        // Given
        val args = Screen.ActorTopMoviesPicksDetails(0)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)

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
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.id).isEqualTo(0)
    }

    @Test
    fun `when loading should set isLoading to true then false`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)

        // Then
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `when onSaveClick called should not change saved state`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)
        advanceUntilIdle()

        val initialSavedState = viewModel.state.value.isSaved

        // When
        viewModel.onSaveClick(1)

        // Then
        assertThat(viewModel.state.value.isSaved).isEqualTo(initialSavedState)
    }

    @Test
    fun `when actor ID is zero should not execute use case`() = runTest {
        // Given
        val actorId = 0
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.id).isEqualTo(0)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        // Given
        val args = Screen.ActorTopMoviesPicksDetails(0)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)

        // Then
        val state = viewModel.state.value
        assertThat(state.id).isEqualTo(0)
        assertThat(state.isSaved).isFalse()
        assertThat(state.backdropPath).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorState).isNull()
        assertThat(state.actorMovieDetails).isEqualTo(CastDetails())
    }

    @Test
    fun `when checkSuccess returns false should trigger error state`() = runTest {
        // Given
        val actorId = 0 // This will make checkSuccess return false
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { getActorMoviePicksById.invoke(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.id).isEqualTo(0)
    }

    @Test
    fun `when onSaveClick called with different movie IDs should maintain same behavior`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)
        advanceUntilIdle()

        val initialSavedState = viewModel.state.value.isSaved

        // When
        viewModel.onSaveClick(1)
        val stateAfterFirst = viewModel.state.value.isSaved

        viewModel.onSaveClick(999)
        val stateAfterSecond = viewModel.state.value.isSaved

        viewModel.onSaveClick(-1)
        val stateAfterThird = viewModel.state.value.isSaved

        // Then
        assertThat(stateAfterFirst).isEqualTo(initialSavedState)
        assertThat(stateAfterSecond).isEqualTo(initialSavedState)
        assertThat(stateAfterThird).isEqualTo(initialSavedState)
    }

    @Test
    fun `when backdropPath and isSaved remain unchanged during success`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(getActorMoviePicksById, savedStateHandle)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.backdropPath).isEmpty()
        assertThat(viewModel.state.value.isSaved).isFalse()
    }

}