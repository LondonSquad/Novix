package com.london.data.remote

import android.util.Log
import com.london.data.datasource.remote.search.ApiConstants
import com.london.data.datasource.remote.search.SearchRemoteDataSourceImpl
import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchRemoteDataSourceImplTest {

    private lateinit var mockHttpClient: HttpClient
    private lateinit var mockHttpResponse: HttpResponse
    private lateinit var searchRemoteDataSource: SearchRemoteDataSourceImpl

    private val json = Json { ignoreUnknownKeys = true }

    private val query = "Game"
    private val includeAdult = false
    private val language = "en"
    private val page = 1

    @Before
    fun setup() {
        mockHttpClient = mockk(relaxed = true)
        mockHttpResponse = mockk(relaxed = true)
        searchRemoteDataSource = SearchRemoteDataSourceImpl(mockHttpClient)

        // Mock Android Log
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `searchForMovies should return ApiSearch of SearchMovieRemote when API call is successful`() = runTest {
        // Given
        val mockResponseBody = createMoviesResponse()
        val expectedResponse = json.decodeFromString<ApiSearch<SearchMovieRemote>>(mockResponseBody)

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        val result = searchRemoteDataSource.searchForMovies(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>())
        }
        coVerify(exactly = 1) {
            mockHttpResponse.bodyAsText()
        }
        verify(exactly = 1) {
            Log.d("TAG", "search: $mockResponseBody")
        }

        assertEquals(expectedResponse.page, result.page)
        assertEquals(expectedResponse.results.size, result.results.size)
        assertEquals(expectedResponse.totalPages, result.totalPages)
        assertEquals(expectedResponse.totalResults, result.totalResults)
        assertEquals("Game", result.results[0].title)
    }

    @Test
    fun `searchForTvShows should return ApiSearch of SearchTvShowRemote when API call is successful`() = runTest {
        // Given
        val query = "Game"
        val includeAdult = false
        val language = "en"
        val page = 1

        val mockResponseBody = createTvShowsResponse()

        val expectedResponse = Json { ignoreUnknownKeys = true }
            .decodeFromString<ApiSearch<SearchTvShowRemote>>(mockResponseBody)

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        val result = searchRemoteDataSource.searchForTvShows(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>())
        }
        coVerify(exactly = 1) {
            mockHttpResponse.bodyAsText()
        }
        verify(exactly = 1) {
            Log.d("TAG", "search: $mockResponseBody")
        }

        assertEquals(expectedResponse.page, result.page)
        assertEquals(expectedResponse.results.size, result.results.size)
        assertEquals(expectedResponse.totalPages, result.totalPages)
        assertEquals(expectedResponse.totalResults, result.totalResults)
        assertEquals("Game of Thrones", result.results[0].name)
    }

    @Test
    fun `searchForActors should return ApiSearch of SearchActorRemote when API call is successful`() = runTest {

        val mockResponseBody = createActorsResponse()

        val expectedResponse = Json { ignoreUnknownKeys = true }
            .decodeFromString<ApiSearch<SearchActorRemote>>(mockResponseBody)

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        val result = searchRemoteDataSource.searchForActors(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>())
        }
        coVerify(exactly = 1) {
            mockHttpResponse.bodyAsText()
        }
        verify(exactly = 1) {
            Log.d("TAG", "search: $mockResponseBody")
        }

        assertEquals(expectedResponse.page, result.page)
        assertEquals(expectedResponse.results.size, result.results.size)
        assertEquals(expectedResponse.totalPages, result.totalPages)
        assertEquals(expectedResponse.totalResults, result.totalResults)
        assertEquals("Robert Downey Jr.", result.results[0].name)
        assertEquals("Acting", result.results[0].knownForDepartment)
    }

    @Test
    fun `searchForMovies should verify correct URL parameters are passed`() = runTest {
        // Given
        val query = "Batman"
        val includeAdult = true
        val language = "es"
        val page = 2
        val mockResponseBody = """{"page": 2, "results": [], "total_pages": 1, "total_results": 0}"""

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        searchRemoteDataSource.searchForMovies(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(match<HttpRequestBuilder.() -> Unit> { builder ->
                val testBuilder = HttpRequestBuilder()
                builder.invoke(testBuilder)

                testBuilder.url.protocol == URLProtocol.Companion.HTTPS &&
                        testBuilder.url.host == ApiConstants.SEARCH_HOST &&
                        testBuilder.url.encodedPath.contains(ApiConstants.SEARCH_PATH_MOVIES) &&
                        testBuilder.url.parameters.contains("query", query) &&
                        testBuilder.url.parameters.contains(
                            "include_adult",
                            includeAdult.toString()
                        ) &&
                        testBuilder.url.parameters.contains("language", language) &&
                        testBuilder.url.parameters.contains("page", page.toString()) &&
                        testBuilder.url.parameters.contains("api_key", "YOUR_API_KEY")
            })
        }
    }

    @Test
    fun `searchForTvShows should verify correct URL parameters are passed`() = runTest {
        // Given
        val query = "Breaking Bad"
        val includeAdult = false
        val language = "fr"
        val page = 3
        val mockResponseBody = """{"page": 3, "results": [], "total_pages": 1, "total_results": 0}"""

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        searchRemoteDataSource.searchForTvShows(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(match<HttpRequestBuilder.() -> Unit> { builder ->
                val testBuilder = HttpRequestBuilder()
                builder.invoke(testBuilder)

                testBuilder.url.protocol == URLProtocol.Companion.HTTPS &&
                        testBuilder.url.host == ApiConstants.SEARCH_HOST &&
                        testBuilder.url.encodedPath.contains(ApiConstants.SEARCH_PATH_TVS) &&
                        testBuilder.url.parameters.contains("query", query) &&
                        testBuilder.url.parameters.contains(
                            "include_adult",
                            includeAdult.toString()
                        ) &&
                        testBuilder.url.parameters.contains("language", language) &&
                        testBuilder.url.parameters.contains("page", page.toString()) &&
                        testBuilder.url.parameters.contains("api_key", "YOUR_API_KEY")
            })
        }
    }

    @Test
    fun `searchForActors should verify correct URL parameters are passed`() = runTest {
        // Given
        val query = "Leonardo DiCaprio"
        val includeAdult = false
        val language = "de"
        val page = 1
        val mockResponseBody = """{"page": 1, "results": [], "total_pages": 1, "total_results": 0}"""

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        searchRemoteDataSource.searchForActors(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(match<HttpRequestBuilder.() -> Unit> { builder ->
                val testBuilder = HttpRequestBuilder()
                builder.invoke(testBuilder)

                testBuilder.url.protocol == URLProtocol.Companion.HTTPS &&
                        testBuilder.url.host == ApiConstants.SEARCH_HOST &&
                        testBuilder.url.encodedPath.contains(ApiConstants.SEARCH_PATH_ACTORS) &&
                        testBuilder.url.parameters.contains("query", query) &&
                        testBuilder.url.parameters.contains(
                            "include_adult",
                            includeAdult.toString()
                        ) &&
                        testBuilder.url.parameters.contains("language", language) &&
                        testBuilder.url.parameters.contains("page", page.toString()) &&
                        testBuilder.url.parameters.contains("api_key", "YOUR_API_KEY")
            })
        }
    }

    @Test
    fun `searchForMovies should handle empty results correctly`() = runTest {
        // Given
        val query = "NonExistentMovie"
        val includeAdult = false
        val language = "en"
        val page = 1
        val mockResponseBody = """
            {
                "page": 1,
                "results": [],
                "total_pages": 1,
                "total_results": 0
            }
        """.trimIndent()

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        val result = searchRemoteDataSource.searchForMovies(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>())
        }
        coVerify(exactly = 1) {
            mockHttpResponse.bodyAsText()
        }

        assertEquals(1, result.page)
        assertEquals(0, result.results.size)
        assertEquals(1, result.totalPages)
        assertEquals(0, result.totalResults)
    }

    @Test
    fun `searchForActors should handle complex KnownFor data correctly`() = runTest {
        // Given
        val query = "Tom Hanks"
        val includeAdult = false
        val language = "en"
        val page = 1
        val mockResponseBody = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "gender": 2,
                        "id": 31,
                        "known_for_department": "Acting",
                        "name": "Tom Hanks",
                        "original_name": "Tom Hanks",
                        "popularity": 78.926,
                        "profile_path": "/path/to/profile.jpg",
                        "known_for": [
                            {
                                "adult": false,
                                "backdrop_path": "/path/to/backdrop1.jpg",
                                "id": 13,
                                "title": "Forrest Gump",
                                "original_title": "Forrest Gump",
                                "overview": "A man with a low IQ has accomplished great things...",
                                "poster_path": "/path/to/poster1.jpg",
                                "media_type": "movie",
                                "original_language": "en",
                                "genre_ids": [35, 18, 10749],
                                "popularity": 432.562,
                                "release_date": "1994-06-23",
                                "video": false,
                                "vote_average": 8.5,
                                "vote_count": 26280
                            },
                            {
                                "adult": false,
                                "backdrop_path": "/path/to/backdrop2.jpg",
                                "id": 862,
                                "title": "Toy Story",
                                "original_title": "Toy Story",
                                "overview": "A cowboy doll is profoundly threatened...",
                                "poster_path": "/path/to/poster2.jpg",
                                "media_type": "movie",
                                "original_language": "en",
                                "genre_ids": [16, 10751, 35],
                                "popularity": 369.594,
                                "release_date": "1995-10-30",
                                "video": false,
                                "vote_average": 8.0,
                                "vote_count": 18237
                            }
                        ]
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()

        coEvery { mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>()) } returns mockHttpResponse
        coEvery { mockHttpResponse.bodyAsText() } returns mockResponseBody

        // When
        val result = searchRemoteDataSource.searchForActors(query, includeAdult, language, page)

        // Then
        coVerify(exactly = 1) {
            mockHttpClient.get(any<HttpRequestBuilder.() -> Unit>())
        }
        coVerify(exactly = 1) {
            mockHttpResponse.bodyAsText()
        }

        assertEquals(1, result.page)
        assertEquals(1, result.results.size)
        assertEquals("Tom Hanks", result.results[0].name)
        assertEquals(2, result.results[0].knownFor.size)
        assertEquals("Forrest Gump", result.results[0].knownFor[0].title)
        assertEquals("Toy Story", result.results[0].knownFor[1].title)
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