package com.london.presentation.feature.details.actor.info.gallery

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorGalleryViewModelTest {

    private lateinit var viewModel: ActorGalleryViewModel
    private lateinit var getActorImagesById: GetActorImagesByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val mockImages = listOf(
        "/image1.jpg",
        "/image2.jpg",
        "/image3.jpg",
        "/image4.jpg"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        getActorImagesById = mockk()
        savedStateHandle = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized with null args should use default actor id`() = runTest {
        // Given
        coEvery { savedStateHandle.getArgs<Screen.ActorGallery>() } returns null
        coEvery { getActorImagesById.invoke(0) } returns emptyList()

        // When
        viewModel = ActorGalleryViewModel(savedStateHandle, getActorImagesById)

        // Then
        assertThat(viewModel.state.value.images).isEmpty()
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.error).isNull()
        coVerify { getActorImagesById.invoke(0) }
    }

    @Test
    fun `when initialized with empty image list should handle correctly`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorGallery(actorId)
        val emptyImages = emptyList<String>()
        coEvery { savedStateHandle.getArgs<Screen.ActorGallery>() } returns args
        coEvery { getActorImagesById.invoke(actorId) } returns emptyImages

        // When
        viewModel = ActorGalleryViewModel(savedStateHandle, getActorImagesById)

        // Then
        assertThat(viewModel.state.value.images).isEmpty()
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `when onBackClick is called should emit NavigateBack effect`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorGallery(actorId)
        coEvery { savedStateHandle.getArgs<Screen.ActorGallery>() } returns args
        coEvery { getActorImagesById.invoke(actorId) } returns mockImages

        viewModel = ActorGalleryViewModel(savedStateHandle, getActorImagesById)

        // When
        viewModel.onBackClick()
    }

}