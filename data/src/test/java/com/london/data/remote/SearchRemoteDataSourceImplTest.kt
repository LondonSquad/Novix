package com.london.data.remote

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.search.SearchRemoteDataSourceImpl
import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
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
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class SearchRemoteDataSourceImplTest {
    private lateinit var httpClient: HttpClient
    private lateinit var searchRemoteDataSource: SearchRemoteDataSourceImpl

    private val json = Json { ignoreUnknownKeys = true }

    private fun setUp(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        val mockEngine = MockEngine {
            handler(it)
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        searchRemoteDataSource = SearchRemoteDataSourceImpl(httpClient)
    }

    @Test
    fun `searchForMovies should return ApiSearch of SearchMovieRemote when API call is successful`() =
        runTest {
            // Given
            val mockResponseBody = createMoviesResponse()
            val expectedResponse =
                json.decodeFromString<ApiSearch<SearchMovieRemote>>(mockResponseBody)

            setUp { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            // When
            val result = searchRemoteDataSource.searchForMovies("Game", false, "en", 1)

            // Then
            assertThat(result.page).isEqualTo(expectedResponse.page)
            assertThat(result.results.size).isEqualTo(expectedResponse.results.size)
            assertThat(result.totalPages).isEqualTo(expectedResponse.totalPages)
            assertThat(result.totalResults).isEqualTo(expectedResponse.totalResults)
            assertThat(result.results[0].title).isEqualTo("Game")
        }

    @Test
    fun `searchForTvShows should return ApiSearch of SearchTvShowRemote when API call is successful`() =
        runTest {
            // Given
            val mockResponseBody = createTvShowsResponse()
            val expectedResponse =
                json.decodeFromString<ApiSearch<SearchTvShowRemote>>(mockResponseBody)

            setUp { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            // When
            val result = searchRemoteDataSource.searchForTvShows("Game", false, "en", 1)

            // Then
            assertThat(result.page).isEqualTo(expectedResponse.page)
            assertThat(result.results.size).isEqualTo(expectedResponse.results.size)
            assertThat(result.totalPages).isEqualTo(expectedResponse.totalPages)
            assertThat(result.totalResults).isEqualTo(expectedResponse.totalResults)
            assertThat(result.results[0].name).isEqualTo("Squid Game")
        }

    @Test
    fun `searchForActors should return ApiSearch of SearchActorRemote when API call is successful`() =
        runTest {
            // Given
            val mockResponseBody = createActorsResponse()
            val expectedResponse =
                json.decodeFromString<ApiSearch<SearchActorRemote>>(mockResponseBody)

            setUp { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            // When
            val result = searchRemoteDataSource.searchForActors("Game", false, "en", 1)

            // Then
            assertThat(result.page).isEqualTo(expectedResponse.page)
            assertThat(result.results.size).isEqualTo(expectedResponse.results.size)
            assertThat(result.totalPages).isEqualTo(expectedResponse.totalPages)
            assertThat(result.totalResults).isEqualTo(expectedResponse.totalResults)
            assertThat(result.results[0].name).isEqualTo("J-One")
            assertThat(result.results[0].knownForDepartment).isEqualTo("Acting")
        }

    @Test
    fun `should handle HTTP error responses correctly`() = runTest {
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
        try {
            searchRemoteDataSource.searchForMovies("Game", false, "en", 1)
            assertThat(false).isTrue() // Should throw exception
        } catch (e: Exception) {
            assertThat(true).isTrue() // Expected behavior
        }
    }

    private fun createMoviesResponse(): String {
        return """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/dvHGj3iLiBaQyc5a1VMm3KsVIsa.jpg",
                        "genre_ids": [9648, 53],
                        "id": 60740,
                        "original_language": "hi",
                        "original_title": "Game",
                        "overview": "Four strangers are invited by the reclusive Kabir Malhotra, to his private island of Samos, Greece. They don't know each other and they don't know him ... and by the next morning they will wish they had never come.",
                        "popularity": 0.8554,
                        "poster_path": "/nB8jtWGzFuYGRY1Dkbtf23dxbap.jpg",
                        "release_date": "2011-04-01",
                        "title": "Game",
                        "video": false,
                        "vote_average": 5.7,
                        "vote_count": 41
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()
    }

    private fun createTvShowsResponse(): String {
        return """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/2meX1nMdScFOoV4370rqHWKmXhY.jpg",
                        "genre_ids": [10759, 9648, 18],
                        "id": 93405,
                        "origin_country": ["KR"],
                        "original_language": "ko",
                        "original_name": "오징어 게임",
                        "overview": "Hundreds of cash-strapped players accept a strange invitation to compete in children's games. Inside, a tempting prize awaits — with deadly high stakes.",
                        "popularity": 1160.79,
                        "poster_path": "/1QdXdRYfktUSONkl1oD5gc6Be0s.jpg",
                        "first_air_date": "2021-09-17",
                        "name": "Squid Game",
                        "vote_average": 7.865,
                        "vote_count": 16246
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()
    }

    private fun createActorsResponse(): String {
        return """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "gender": 0,
                        "id": 3397332,
                        "known_for_department": "Acting",
                        "name": "J-One",
                        "original_name": "J-One",
                        "popularity": 0.0218,
                        "profile_path": null,
                        "known_for": [
                            {
                                "adult": false,
                                "backdrop_path": null,
                                "id": 52887,
                                "title": "Loyalty & Respect",
                                "original_title": "Loyalty and Respect",
                                "overview": "In Cashville, two friend friends are torn apart buy fast money and launch an all out street war where no one can be trusted. Loyalty and Respect is lost over Cash in the city of Cashville.",
                                "poster_path": "/yOAoNwqUFTFTQh2csSIsws3O9f3.jpg",
                                "media_type": "movie",
                                "original_language": "en",
                                "genre_ids": [28, 12, 35, 80, 18, 10402],
                                "popularity": 0.1266,
                                "release_date": "2006-01-24",
                                "video": false,
                                "vote_average": 7,
                                "vote_count": 1
                            }
                        ]
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()
    }
}