package com.london.data.mapper.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.mapper.home.popular.toPopularMovie
import com.london.data.mapper.home.popular.toPopularMovies
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.domain.entity.popular.PopularMovie
import org.junit.Test

class PopularMovieMapperTest {

    @Test
    fun `when map toPopularMovie should map all fields correctly`() {
        // When
        val result: PopularMovie = samplePopularMovieResponse.toPopularMovie()

        // Then
        assertThat(result.id).isEqualTo(123)
        assertThat(result.title).isEqualTo("Inception")
        assertThat(result.posterUrl).contains("/poster.jpg")
        assertThat(result.rating).isEqualTo(8.7)
    }

    @Test
    fun `when map toPopularMovies should map list of items correctly`() {
        // Given
        val apiResponse = ApiResponse(
            items = listOf(samplePopularMovieResponse, anotherPopularMovieResponse),
            totalPages = 10,
            totalItems = 200,
            currentPage = 1
        )
        // When
        val result: List<PopularMovie> = apiResponse.toPopularMovies()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(123)
        assertThat(result[1].title).isEqualTo("Interstellar")
    }

    @Test
    fun `when map toPopularMovie should handles null fields gracefully`() {

        // When
        val result = nullFieldsResponse.toPopularMovie()

        // Then
        assertThat(result.id).isEqualTo(0)
        assertThat(result.title).isEmpty()
        assertThat(result.posterUrl).isEmpty()
        assertThat(result.rating).isEqualTo(0.0)
    }

    companion object {
        val nullFieldsResponse = PopularMovieResponse(
            adult = null,
            backdropPath = null,
            genreIds = null,
            id = null,
            originalLanguage = null,
            originalTitle = null,
            overview = null,
            popularity = null,
            posterPath = null,
            releaseDate = null,
            title = null,
            video = null,
            voteAverage = null,
            voteCount = null
        )


        val samplePopularMovieResponse = PopularMovieResponse(
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2, 3),
            id = 123,
            originalLanguage = "en",
            originalTitle = "Inception",
            overview = "A mind-bending thriller",
            popularity = 500.0,
            posterPath = "/poster.jpg",
            releaseDate = "2010-07-16",
            title = "Inception",
            video = false,
            voteAverage = 8.7,
            voteCount = 15000
        )

        val anotherPopularMovieResponse = PopularMovieResponse(
            adult = false,
            backdropPath = "/backdrop2.jpg",
            genreIds = listOf(4, 5),
            id = 456,
            originalLanguage = "en",
            originalTitle = "Interstellar",
            overview = "A journey through space and time",
            popularity = 600.0,
            posterPath = "/poster2.jpg",
            releaseDate = "2014-11-07",
            title = "Interstellar",
            video = false,
            voteAverage = 8.6,
            voteCount = 20000
        )
    }
}
