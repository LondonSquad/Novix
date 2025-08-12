package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actordetails.cast.CastActorEntity
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
    private val testDispatcher = StandardTestDispatcher()

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
    fun `onSaveClick should toggle saved state when it is called`() = runTest {
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
    fun `save state should toggled multiple times to maintain consistency`() = runTest {
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
    fun `onSaveClick should still toggle same saved state when it is called with different movieIds `() = runTest {
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
            assertThat(isSaved).isFalse()
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
        viewModel.onRetryClick()
        viewModel.onBackClick()
        viewModel.onSaveClick(1)
        viewModel.onMovieClick(1)
    }
}
