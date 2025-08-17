package com.london.data.mapper.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.mapper.home.popular.toPopularMovie
import com.london.data.mapper.home.popular.toPopularMovies
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.domain.entity.popular.PopularMedia
import org.junit.Test

class PopularMovieMapperTest {

    @Test
    fun `when map toPopularMovie should map all fields correctly`() {
        // When
        val result: PopularMedia = samplePopularMovieResponse.toPopularMovie()

        // Then
        assertThat(result.id).isEqualTo(123)
        assertThat(result.name).isEqualTo("Inception")
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
        val result: List<PopularMedia> = apiResponse.toPopularMovies()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(123)
        assertThat(result[1].name).isEqualTo("Interstellar")
    }

    @Test
    fun `when map toPopularMovie should handles null fields gracefully`() {

        // When
        val result = nullFieldsResponse.toPopularMovie()

        // Then
        assertThat(result.id).isEqualTo(0)
        assertThat(result.name).isEmpty()
        assertThat(result.posterUrl).isEmpty()
        assertThat(result.rating).isEqualTo(0.0)
    }

    companion object {
        val nullFieldsResponse = PopularMovieResponse(
            id = null,
            posterPath = null,
            title = null,
            voteAverage = null,
        )


        val samplePopularMovieResponse = PopularMovieResponse(
            id = 123,
            posterPath = "/poster.jpg",
            title = "Inception",
            voteAverage = 8.7,
        )

        val anotherPopularMovieResponse = PopularMovieResponse(
            id = 456,
            posterPath = "/poster2.jpg",
            title = "Interstellar",
            voteAverage = 8.6,
        )
    }
}
