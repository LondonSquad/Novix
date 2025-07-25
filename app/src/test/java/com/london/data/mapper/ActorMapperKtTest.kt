package com.london.data.mapper

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.search.KnownForDtoLocal
import com.london.data.local.model.search.PersonDtoLocal
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.KnownFor
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.util.generateHash
import com.london.data.mapper.trending.toActorEntity
import com.london.data.mapper.trending.toKnownForDtoLocal
import com.london.data.mapper.trending.toLocal
import com.london.domain.entity.Actor
import org.junit.Test

class ActorMapperKtTest {

    @Test
    fun `PersonDtoLocal toActorEntity maps correctly`() {
        // Given
        val personDto = createCompletePersonDtoLocal()

        // When
        val result = personDto.toActorEntity()

        // Then
        assertThat(result).isEqualTo(
            Actor(
                id = 123,
                name = "John Doe",
                profilePicture = "https://image.tmdb.org/t/p/w500/profile.jpg"
            )
        )
    }

    @Test
    fun `ApiResponse toLocal maps correctly`() {
        // Given
        val apiResponse = createCompleteApiResponse()
        val query = "test query"
        val expectedQueryHash = query.generateHash()
        // When
        val result = apiResponse.toLocal(query)

        // Then
        assertThat(result).isEqualTo(
            SearchActorsLocal(
                date = result.date,
                query = expectedQueryHash,
                page = 1,
                results = listOf(
                    PersonDtoLocal(
                        id = 123,
                        name = "John Doe",
                        profileUrl = "/profile.jpg",
                        adult = true,
                        gender = 2,
                        knownForDepartment = "Acting",
                        originalName = "Johnathan Doe",
                        popularity = 7.5,
                        knownFor = listOf(
                            KnownForDtoLocal(
                                id = 1,
                                title = "Inception",
                                adult = false,
                                backdropPath = "/backdrop.jpg",
                                originalTitle = "Inception",
                                overview = "Dream stealing movie",
                                posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
                                mediaType = "movie",
                                originalLanguage = "en",
                                genreIds = listOf(1, 2),
                                popularity = 8.5,
                                releaseDate = "2010-07-16",
                                video = false,
                                voteAverage = 8.8,
                                voteCount = 10000,
                                name = null,
                                originalName = null,
                                firstAirDate = null,
                                originCountry = null
                            )
                        )
                    ),
                ),
                id = 0,
                totalPages = 5,
                totalResults = 50
            )
        )
    }

    @Test
    fun `KnownFor toKnownForDtoLocal maps correctly`() {
        // Given
        val knownFor = createCompleteKnownForRemote()

        // When
        val result = knownFor.toKnownForDtoLocal()

        // Then
        assertThat(result).isEqualTo(
            KnownForDtoLocal(
                id = 1,
                title = "Inception",
                adult = false,
                backdropPath = "/backdrop.jpg",
                originalTitle = "Inception",
                overview = "Dream stealing movie",
                posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
                mediaType = "movie",
                originalLanguage = "en",
                genreIds = listOf(1, 2),
                popularity = 8.5,
                releaseDate = "2010-07-16",
                video = false,
                voteAverage = 8.8,
                voteCount = 10000,
                name = null,
                originalName = null,
                firstAirDate = null,
                originCountry = null
            )
        )
    }

    companion object {

        fun createCompletePersonDtoLocal(): PersonDtoLocal = PersonDtoLocal(
            id = 123,
            name = "John Doe",
            profileUrl = "/profile.jpg",
            adult = true,
            gender = 2,
            knownForDepartment = "Acting",
            originalName = "Johnathan Doe",
            popularity = 7.5,
            knownFor = listOf(createCompleteKnownForDtoLocal())
        )

        private fun createCompleteKnownForDtoLocal(): KnownForDtoLocal = KnownForDtoLocal(
            id = 1,
            title = "Inception",
            adult = false,
            backdropPath = "/backdrop.jpg",
            originalTitle = "Inception",
            overview = "Dream stealing movie",
            posterUrl = "/poster.jpg",
            mediaType = "movie",
            originalLanguage = "en",
            genreIds = listOf(1, 2),
            popularity = 8.5,
            releaseDate = "2010-07-16",
            video = false,
            voteAverage = 8.8,
            voteCount = 10000,
            name = null,
            originalName = null,
            firstAirDate = null,
            originCountry = null
        )

        private fun createCompleteSearchActorRemote(): SearchActorRemote = SearchActorRemote(
            id = 123,
            name = "John Doe",
            adult = true,
            gender = 2,
            knownForDepartment = "Acting",
            originalName = "Johnathan Doe",
            popularity = 7.5,
            profilePath = "/profile.jpg",
            knownFor = listOf(createCompleteKnownForRemote())
        )

        private fun createCompleteKnownForRemote(): KnownFor = KnownFor(
            id = 1,
            title = "Inception",
            adult = false,
            backdropPath = "/backdrop.jpg",
            originalTitle = "Inception",
            overview = "Dream stealing movie",
            posterPath = "/poster.jpg",
            mediaType = "movie",
            originalLanguage = "en",
            genreIds = listOf(1, 2),
            popularity = 8.5,
            releaseDate = "2010-07-16",
            video = false,
            voteAverage = 8.8,
            voteCount = 10000,
            name = null,
            originalName = null,
            firstAirDate = null,
            originCountry = null
        )

        private fun createCompleteApiResponse(): ApiResponse<SearchActorRemote> = ApiResponse(
            items = listOf(createCompleteSearchActorRemote()),
            currentPage = 1,
            totalPages = 5,
            totalItems = 50
        )
    }
}