package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.MovieSearchFailedException
import com.london.domain.entity.Movie
import com.london.domain.repo.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesUseCaseTest {
    lateinit var searchRepository: SearchRepository
    lateinit var getMoviesUseCase: GetMoviesUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        getMoviesUseCase = GetMoviesUseCase(searchRepository)
    }

    @Test
    fun `should return a list of movies when repository return a list of actors`() = runTest {
        //given
        coEvery { searchRepository.searchForMovies(NAME, LANGUAGE) } returns listOf(movie)
        //when
        val result = getMoviesUseCase(NAME, LANGUAGE)
        //then
        assertThat(result).isEqualTo(listOf(movie))
    }

    @Test
    fun `should throw an exception when repository throws an exception`() = runTest {
        //given
        coEvery { searchRepository.searchForMovies(NAME, LANGUAGE) } throws MovieSearchFailedException()
        //when //then
        assertThrows<MovieSearchFailedException> {
            getMoviesUseCase(NAME, LANGUAGE)
        }
    }


    private companion object {
        const val NAME = "Movie"
        const val LANGUAGE = "en-US"
        val movie = Movie(
            id = 1,
            name = NAME,
            posterPicture = ""
        )
    }
}