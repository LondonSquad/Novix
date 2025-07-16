package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetMovieImagesFailedException
import com.london.domain.repository.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieImagesUseCaseTest {
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var getMovieImageUseCase: GetMovieImagesUseCase

    @Before
    fun setUp() {
        movieDetailsRepository = mockk()
        getMovieImageUseCase = GetMovieImagesUseCase(movieDetailsRepository)
    }

    @Test
    fun `should return similar moviesList when repository returns data`() = runTest {
        // given
        coEvery { movieDetailsRepository.getMovieImagesById(MOVIE_ID) } returns movieMockImages
        // when
        val result = getMovieImageUseCase(MOVIE_ID)
        // then
        assertThat(result).isEqualTo(movieMockImages)
    }

    @Test
    fun `should return empty moviesList when repository return empty list`() = runTest {
        // given
        coEvery { movieDetailsRepository.getMovieImagesById(MOVIE_ID) } returns emptyList()
        // when
        val result = getMovieImageUseCase(MOVIE_ID)
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw an exception when repository throws an exception`() = runTest {
        // given
        coEvery { movieDetailsRepository.getMovieImagesById(MOVIE_ID) } throws GetMovieImagesFailedException()

        // when & then
        assertThrows<GetMovieImagesFailedException> {
            getMovieImageUseCase(MOVIE_ID)
        }
    }

    @Test
    fun `should limit the number of images returned when images over 10`() = runTest {
        // given
        coEvery { movieDetailsRepository.getMovieImagesById(MOVIE_ID) } returns movieMockImages
        // when
        val result = getMovieImageUseCase(MOVIE_ID)
        // then
        assertThat(result).hasSize(10)
    }

    private companion object {
        const val MOVIE_ID = 123

        val movieMockImages = listOf(
            "/images/movie1.jpg",
            "/images/movie2.jpg",
            "/images/movie3.jpg",
            "/images/movie4.jpg",
            "/images/movie5.jpg",
            "/images/movie6.jpg",
            "/images/movie8.jpg",
            "/images/movie9.jpg",
            "/images/movie10.jpg",
            "/images/movie11.jpg",
        )
    }
}