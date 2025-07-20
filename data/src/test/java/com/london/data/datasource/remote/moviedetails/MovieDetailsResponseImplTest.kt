package com.london.data.datasource.remote.moviedetails

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemoteImpl
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieImages
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class MovieDetailsResponseImplTest {

    private lateinit var httpClient: HttpClient
    private lateinit var remote: MovieDetailsRemoteImpl
    private lateinit var deviceConfigurationDataSource: DeviceConfigurationDataSource

    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setupLogs() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        deviceConfigurationDataSource = mockk(relaxed = true)
    }

    private fun setUp(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        val mockEngine = MockEngine {
            handler(it)
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        remote = MovieDetailsRemoteImpl(httpClient, deviceConfigurationDataSource)
    }

    @Test
    fun `getMovieDetails should return MovieDetailsRemote when API call is successful`() = runTest {
        // Given
        val mockResponseBody = createMovieDetailsResponse()
        val expectedResponse = json.decodeFromString<MovieDetailsResponse>(mockResponseBody)

        setUp { _ ->
            respond(
                content = mockResponseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = remote.getMovieDetails(1)

        // Then
        assertThat(result.id).isEqualTo(expectedResponse.id)
        assertThat(result.title).isEqualTo(expectedResponse.title)
        assertThat(result.overview).isEqualTo(expectedResponse.overview)
        assertThat(result.releaseDate).isEqualTo(expectedResponse.releaseDate)
        assertThat(result.runtime).isEqualTo(expectedResponse.runtime)
        assertThat(result.voteAverage).isEqualTo(expectedResponse.voteAverage)
    }

    @Test
    fun `getMovieDetails should throw exception when API call fails`() = runTest {
        // Given
        val errorResponseBody =
            """{"status_message": "The resource you requested could not be found.", "status_code": 34}"""

        setUp { _ ->
            respond(
                content = errorResponseBody,
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When/Then
        assertThrows<Exception> {
            remote.getMovieDetails(999)
        }
    }

    @Test
    fun `getSimilarMovies should return SimilarMovies when API call is successful`() = runTest {
        // Given
        val mockResponseBody = createSimilarMoviesResponse()
        val expectedResponse = json.decodeFromString<SimilarMoviesResponse>(mockResponseBody)

        setUp { _ ->
            respond(
                content = mockResponseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = remote.getSimilarMovies(1)

        // Then
        assertThat(result.page).isEqualTo(expectedResponse.page)
        assertThat(result.similarMovieRemotes?.size).isEqualTo(expectedResponse.similarMovieRemotes?.size)
        assertThat(result.totalPages).isEqualTo(expectedResponse.totalPages)
        assertThat(result.totalResults).isEqualTo(expectedResponse.totalResults)
        if (!result.similarMovieRemotes.isNullOrEmpty()) {
            result.similarMovieRemotes?.let { similarMovies ->
                assertThat(similarMovies[0].title).isEqualTo("The Matrix")
            }
        }
    }

    @Test
    fun `getSimilarMovies should throw exception when API call fails`() = runTest {
        // Given
        val errorResponseBody = """{"status_message": "Invalid API key", "status_code": 401}"""

        setUp { _ ->
            respond(
                content = errorResponseBody,
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When/Then
        assertThrows<Exception> {
            remote.getSimilarMovies(1)
        }
    }

    @Test
    fun `getMovieCast should return MovieCastRemote when API call is successful`() = runTest {
        // Given
        val mockResponseBody = createMovieCastResponse()
        val expectedResponse = json.decodeFromString<MovieCastResponse>(mockResponseBody)

        setUp { _ ->
            respond(
                content = mockResponseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = remote.getMovieCast(1)

        // Then
        assertThat(result.id).isEqualTo(expectedResponse.id)
        assertThat(result.actorRemote?.size).isEqualTo(expectedResponse.actorRemote?.size)
        assertThat(result.crew?.size).isEqualTo(expectedResponse.crew?.size)
        if (result.actorRemote?.isNotEmpty() == true) {
            assertThat(result.actorRemote!![0].name).isEqualTo("Leonardo DiCaprio")
            assertThat(result.actorRemote!![0].character).isEqualTo("Cobb")
        }
        if (result.crew?.isNotEmpty() == true) {
            assertThat(result.crew!![0].name).isEqualTo("Christopher Nolan")
            assertThat(result.crew!![0].job).isEqualTo("Director")
        }
    }

    @Test
    fun `getMovieCast should throw exception when API call fails`() = runTest {
        // Given
        val errorResponseBody = """{"status_message": "Server error", "status_code": 500}"""

        setUp { _ ->
            respond(
                content = errorResponseBody,
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When/Then
        assertThrows<Exception> {
            remote.getMovieCast(1)
        }
    }

    @Test
    fun `getMovieImages should return MovieImages when API call is successful`() = runTest {
        // Given
        val mockResponseBody = createMovieImagesResponse()
        val expectedResponse = json.decodeFromString<MovieImages>(mockResponseBody)

        setUp { _ ->
            respond(
                content = mockResponseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = remote.getMovieImages(1)

        // Then
        assertThat(result.id).isEqualTo(expectedResponse.id)
        assertThat(result.backdrops?.size).isEqualTo(expectedResponse.backdrops?.size)
        assertThat(result.posters?.size).isEqualTo(expectedResponse.posters?.size)
        assertThat(result.logos?.size).isEqualTo(expectedResponse.logos?.size)
    }

    @Test
    fun `getMovieImages should throw exception when API call fails`() = runTest {
        // Given
        val errorResponseBody = """{"status_message": "Network timeout", "status_code": 408}"""

        setUp { _ ->
            respond(
                content = errorResponseBody,
                status = HttpStatusCode.RequestTimeout,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When/Then
        assertThrows<Exception> {
            remote.getMovieImages(1)
        }
    }

    private fun createMovieDetailsResponse(): String {
        return """
            {
                "id": 1,
                "title": "Inception",
                "overview": "Dream inside a dream",
                "release_date": "2010-07-16",
                "runtime": 148,
                "vote_average": 8.8,
                "genres": [],
                "backdrop_path": "/backdrop.jpg",
                "poster_path": "/poster.jpg",
                "adult": false,
                "belongs_to_collection": null,
                "budget": 160000000,
                "homepage": "https://inception.movie",
                "imdb_id": "tt1375666",
                "origin_country": ["US"],
                "original_language": "en",
                "original_title": "Inception",
                "popularity": 123.4,
                "production_companies": [],
                "production_countries": [],
                "revenue": 825000000,
                "spoken_languages": [],
                "status": "Released",
                "tagline": "Your mind is the scene of the crime",
                "video": false,
                "vote_count": 9999
            }
        """.trimIndent()
    }

    private fun createSimilarMoviesResponse(): String {
        return """
            {
                "page": 1,
                "results": [
                    {
                        "id": 603,
                        "title": "The Matrix",
                        "overview": "Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.",
                        "release_date": "1999-03-30",
                        "adult": false,
                        "backdrop_path": "/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
                        "genre_ids": [28, 878],
                        "original_language": "en",
                        "original_title": "The Matrix",
                        "popularity": 51.037,
                        "poster_path": "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                        "video": false,
                        "vote_average": 8.2,
                        "vote_count": 11742
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()
    }

    private fun createMovieCastResponse(): String {
        return """
            {
                "id": 1,
                "cast": [
                    {
                        "adult": false,
                        "gender": 2,
                        "id": 100,
                        "known_for_department": "Acting",
                        "name": "Leonardo DiCaprio",
                        "original_name": "Leonardo DiCaprio",
                        "popularity": 99.9,
                        "profile_path": "/leo.jpg",
                        "cast_id": 1,
                        "character": "Cobb",
                        "credit_id": "abc123",
                        "order": 0
                    }
                ],
                "crew": [
                    {
                        "adult": false,
                        "gender": 2,
                        "id": 200,
                        "known_for_department": "Directing",
                        "name": "Christopher Nolan",
                        "original_name": "Christopher Nolan",
                        "popularity": 80.0,
                        "profile_path": "/nolan.jpg",
                        "credit_id": "crew123",
                        "department": "Directing",
                        "job": "Director"
                    }
                ]
            }
        """.trimIndent()
    }

    private fun createMovieImagesResponse(): String {
        return """
            {
                "id": 1,
                "backdrops": [],
                "posters": [],
                "logos": []
            }
        """.trimIndent()
    }
}