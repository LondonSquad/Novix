package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.mapper.details.tvshow.TvShowImagesMapper.toEntity
import com.london.data.mapper.details.tvshow.toEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodesEntity
import com.london.data.mapper.home.toprated.toEntity
import com.london.data.mapper.search.toReviewEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.tvshow.model.ImageItem
import com.london.data.remote.model.details.tvshow.model.TvShowCreator
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowEpisode
import com.london.data.remote.model.details.tvshow.model.TvShowGenre
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowNetwork
import com.london.data.remote.model.details.tvshow.model.TvShowProductionCompany
import com.london.data.remote.model.details.tvshow.model.TvShowProductionCountry
import com.london.data.remote.model.details.tvshow.model.TvShowSeason
import com.london.data.remote.model.details.tvshow.model.TvShowSpokenLanguage
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeCrewMember
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeGuestStar
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeVideoProviderRemote
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeVideoResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoRemote
import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoResponse
import com.london.data.remote.model.home.popular.PopularTvShowResponse
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.reviews.AuthorDetailsResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.source.tvshow.TvShowRemoteDataSource
import com.london.data.repository.tvshow.TvShowRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TvShowRepositoryImplTest {

    private lateinit var remoteDataSource: TvShowRemoteDataSource
    private lateinit var repository: TvShowRepositoryImpl
    private lateinit var tvShowRemoteDataSource: TvShowRemoteDataSource
    private lateinit var homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>
    private lateinit var localTopRated: HomeLocalDataSource<TopRatedLocal>
    private lateinit var crashReporter: CrashReporter
    private lateinit var authPreferences: AuthPreferences

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        homeLocalDataSource = mockk(relaxed = true)
        localTopRated = mockk(relaxed = true)
        crashReporter = mockk(relaxed = true)
        tvShowRemoteDataSource = mockk(relaxed = true)

        repository = TvShowRepositoryImpl(
            remoteDataSource,
            authPreferences = authPreferences,
            homeLocalDataSource = homeLocalDataSource,
            localTopRated = localTopRated,
            crashReporter = crashReporter,
        )
    }

    @Test
    fun `getTvShowDetailsById should return TvShowDetailsEntity when remote call succeeds`() =
        runTest {
            coEvery { remoteDataSource.getTvShowDetailsById(TV_SHOW_ID) }.returns(
                Result.success(TvShowDetailsRemoteMock)
            )

            val result = repository.getTvShowDetailsById(TV_SHOW_ID)

            assertThat(result).isEqualTo(TvShowDetailsRemoteMock.toEntity())
        }

    @Test
    fun `getImagesTvShowById should return TvShowImagesEntity when remote call succeeds`() =
        runTest {
            coEvery { remoteDataSource.getTvShowImagesById(TV_SHOW_ID) }.returns(
                Result.success(
                    TvShowImagesRemoteMock
                )
            )

            val result = repository.getImagesTvShowById(TV_SHOW_ID)

            assertThat(result).isEqualTo(TvShowImagesRemoteMock.toEntity())
        }

    @Test
    fun `getTvShowDetailsById should throw ValidationException when remote fails`() = runTest {
        coEvery {
            remoteDataSource.getTvShowDetailsById(123)
        } throws NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowDetailsById(123)
        }
    }

    @Test
    fun `getTvShowImagesById should throw TimeoutException when remote fails`() = runTest {
        coEvery {
            remoteDataSource.getTvShowImagesById(123)
        } throws NetworkException.TimeoutException("timeout error")

        assertThrows<NetworkException.TimeoutException> {
            repository.getImagesTvShowById(123)
        }
    }

    @Test
    fun `getTvShowEpisodesBySeason should throw HttpLockedException when remote fails`() = runTest {
        coEvery {
            remoteDataSource.getTvShowEpisodesBySeason(
                seasonNumber = 0, id = 123
            )
        } throws NetworkException.HttpLockedException("HttpLocked error")

        assertThrows<NetworkException.HttpLockedException> {
            repository.getTvShowEpisodesBySeason(123, 0)
        }
    }

    @Test
    fun `getEpisodeDetailsByPosition should throw ServerErrorException when remote fails`() =
        runTest {
            coEvery {
                remoteDataSource.getEpisodeDetails(
                    tvShowId = 123, seasonNumber = 0, episodeNumber = 0
                )
            } throws NetworkException.ServerErrorException("server error")

            assertThrows<NetworkException.ServerErrorException> {
                repository.getTvShowEpisodeByPosition(123, 0, 0)
            }
        }

    @Test
    fun `getTvShowEpisodesBySeason should return TvShowEpisodesEntity when remote call succeeds`() =
        runTest {
            coEvery {
                remoteDataSource.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)
            }.returns(Result.success(TvShowEpisodesRemoteMock))

            val result = repository.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)

            assertThat(result).isEqualTo(TvShowEpisodesRemoteMock.toTvShowEpisodesEntity())
        }

    @Test
    fun `getTvShowEpisodesBySeason should throw original exception when remote call fails`() =
        runTest {
            val networkException = RuntimeException("Network error")
            coEvery {
                remoteDataSource.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)
            }.throws(networkException)

            val actualException = assertThrows<RuntimeException> {
                repository.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)
            }

            assertThat(actualException).isEqualTo(networkException)
        }

    @Test
    fun `getTvShowEpisodeByPosition should throw original exception when remote call fails`() =
        runTest {
            val networkException = RuntimeException("Network error")
            coEvery {
                remoteDataSource.getEpisodeDetails(
                    TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER
                )
            } throws networkException

            val actualException = assertThrows<RuntimeException> {
                repository.getTvShowEpisodeByPosition(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
            }

            assertThat(actualException).isEqualTo(networkException)
        }

    @Test
    fun `getEpisodeVideos should return list of YouTube URLs when remote call succeeds`() =
        runTest {
            // Given
            coEvery {
                remoteDataSource.getEpisodeVideos(
                    tvShowId = TV_SHOW_ID,
                    seasonNumber = SEASON_NUMBER,
                    episodeNumber = EPISODE_NUMBER
                )
            }.returns(Result.success(EpisodeVideoResponseMock))

            // When
            val result = repository.getEpisodeVideos(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

            // Then
            val expectedUrls = listOf(
                "dQw4w9WgXcQ".asYoutubeUrlOrEmpty(), "abc123def456".asYoutubeUrlOrEmpty()
            )
            assertThat(result).isEqualTo(expectedUrls)
        }

    @Test
    fun `getEpisodeVideos should return empty list when remote returns null results`() = runTest {
        // Given
        val mockVideoResponse = EpisodeVideoResponse(
            id = TV_SHOW_ID, results = null
        )

        coEvery {
            remoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID, seasonNumber = SEASON_NUMBER, episodeNumber = EPISODE_NUMBER
            )
        }.returns(Result.success(mockVideoResponse))

        // When
        val result = repository.getEpisodeVideos(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getEpisodeVideos should return empty list when remote returns empty results`() = runTest {
        // Given
        val mockVideoResponse = EpisodeVideoResponse(
            id = TV_SHOW_ID, results = emptyList()
        )

        coEvery {
            remoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID, seasonNumber = SEASON_NUMBER, episodeNumber = EPISODE_NUMBER
            )
        }.returns(Result.success(mockVideoResponse))

        // When
        val result = repository.getEpisodeVideos(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getEpisodeVideos should throw NetworkException when remote call fails`() = runTest {
        // Given
        val networkException = NetworkException.ServerErrorException("Server error")

        coEvery {
            remoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID, seasonNumber = SEASON_NUMBER, episodeNumber = EPISODE_NUMBER
            )
        }.throws(networkException)

        // When & Then
        val actualException = assertThrows<NetworkException.ServerErrorException> {
            repository.getEpisodeVideos(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        }

        assertThat(actualException).isEqualTo(networkException)
    }

    @Test
    fun `getTvShowReviews should throw UnAuthorizedException when API returns 401`() = runTest {
        val tvShowId = 456
        val page = 1

        coEvery {
            remoteDataSource.getTvShowReviews(tvShowId, page)
        } throws NetworkException.UnAuthorizedException("401 Unauthorized")

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getTvShowReviews(tvShowId, page)
        }
    }

    @Test
    fun `getTvShowReviews should throw TimeoutException when API times out`() = runTest {
        val tvShowId = 456
        val page = 1

        coEvery {
            remoteDataSource.getTvShowReviews(tvShowId, page)
        } throws NetworkException.TimeoutException("Request timed out")

        assertThrows<NetworkException.TimeoutException> {
            repository.getTvShowReviews(tvShowId, page)
        }
    }

    @Test
    fun `getTvShowReviews should throw HttpLockedException when API returns 423`() = runTest {
        val tvShowId = 456
        val page = 1

        coEvery {
            remoteDataSource.getTvShowReviews(tvShowId, page)
        } throws NetworkException.HttpLockedException("Resource locked")

        assertThrows<NetworkException.HttpLockedException> {
            repository.getTvShowReviews(tvShowId, page)
        }
    }

    @Test
    fun `getTvShowReviews should throw ValidationException when API returns 422`() = runTest {
        val tvShowId = 456
        val page = 1

        coEvery {
            remoteDataSource.getTvShowReviews(tvShowId, page)
        } throws NetworkException.ValidationException("Invalid data")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowReviews(tvShowId, page)
        }
    }

    @Test
    fun `getTvShowReviews should return mapped reviews when remote succeeds`() = runTest {
        // Arrange
        val fakeRemoteResponse = ApiResponse(
            currentPage = 1, items = listOf(
                ReviewResponse(
                    id = "review1",
                    author = "Author 1",
                    content = "This is review 1",
                    createdAt = "2024-01-01",
                    updatedAt = "2024-01-02",
                    authorDetailsResponse = AuthorDetailsResponse(
                        authorName = "John Doe",
                        authorUsername = "johndoe",
                        authorPictureUrl = "/profile.jpg",
                        rating = 4.5
                    ),
                    url = "https://example.com/review1"
                )
            ), totalPages = 1, totalItems = 1
        )

        coEvery { remoteDataSource.getTvShowReviews(TV_SHOW_ID, PAGE_NUMBER) }.returns(
            Result.success(fakeRemoteResponse)
        )

        val result = repository.getTvShowReviews(TV_SHOW_ID, PAGE_NUMBER)

        val expected = fakeRemoteResponse.toReviewEntity()

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getAccountTvShowState returns correct MediaStates`() = runTest {
        // Given
        val seriesId = 456
        coEvery {
            remoteDataSource.getAccountTvShowStates(
                tvShowId = seriesId, guestSessionId = GUSET_SESSION, userSessionId = USER_SESSION
            )
        } returns Result.success(mediaStatesDto)
        every { authPreferences.getGuestSessionId() } returns GUSET_SESSION
        every { authPreferences.getSessionId() } returns USER_SESSION

        // When
        val result = repository.getAccountTvShowState(seriesId)

        // Then
        assertEquals(expectedEntity, result)
    }

    @Test
    fun `getAccountTvEpisode returns correct MediaStates`() = runTest {
        // Given
        val seriesId = 789
        val seasonNumber = 1
        val episodeNumber = 2

        coEvery {
            remoteDataSource.getAccountTvEpisodeState(
                tvShowId = seriesId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                guestSessionId = GUSET_SESSION,
                userSessionId = USER_SESSION
            )
        } returns Result.success(mediaStatesDto)
        every { authPreferences.getGuestSessionId() } returns GUSET_SESSION
        every { authPreferences.getSessionId() } returns USER_SESSION

        // When
        val result = repository.getAccountTvEpisode(seriesId, seasonNumber, episodeNumber)

        // Then
        assertEquals(expectedEntity, result)
    }


    @Test
    fun `getTvShowVideos should map remote video list correctly`() = runTest {
        // Given
        val tvShowId = 123
        coEvery { remoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
            fakeTvShowVideosResponse()
        )

        // When
        val result: List<String> = repository.getTvShowVideos(tvShowId)

        // Then
        assertThat(result).hasSize(2)

        val firstVideo = result.first()
        assertThat(firstVideo).isEqualTo(fakeTvShowVideosResponse().tvShow?.get(0)?.key.asYoutubeUrlOrEmpty())

        val secondVideo = result[1]
        assertThat(secondVideo).isEqualTo(
            fakeTvShowVideosResponse().tvShow?.get(1)?.key.asYoutubeUrlOrEmpty()
        )
    }

    @Test
    fun `getTvShowVideos should return empty list when API returns null list`() = runTest {
        // Given
        val tvShowId = 999
        coEvery { remoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
            fakeNullTvShowVideosResponse()
        )

        // When
        val result = repository.getTvShowVideos(tvShowId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowVideos should throw ValidationException when remote fails`() = runTest {

        val tvShowId = 123
        coEvery {
            remoteDataSource.getTvShowVideos(tvShowId)
        } throws NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowVideos(tvShowId)
        }
    }

    @Test
    fun `getTrendingTvShows should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { remoteDataSource.getTrendingTvShows(any()) } returns Result.failure(
            error
        )

        // When
        try {
            repository.getTrendingTvShows(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            Assert.assertEquals("Network error", e.message)
        }
    }


    @Test
    fun `getTrendingTvShows should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingTvShowsApiResponse()
        coEvery { remoteDataSource.getTrendingTvShows(any()) } returns Result.success(
            mockApiResponse
        )

        // When
        val result1 = repository.getTrendingTvShows(page = 1)
        val result2 = repository.getTrendingTvShows(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        Assert.assertEquals(1, result1.currentPage)
        Assert.assertEquals(1, result2.currentPage)
    }


    @Test
    fun `getTopRatedTvSeries should return paged response with correct data`() = runTest {
        // Given
        val expectedApiResponse = fakeApiResponseWithTvSeries()
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(expectedApiResponse)

        coEvery {
            localTopRated.getAll()
        } returns emptyList()

        // When
        val result: PagedFetchResponse<TopRatedMedia> = repository.getTopRatedTvShows(PAGE)

        // Then
        assertThat(result.items).hasSize(2)
        assertThat(result.currentPage).isEqualTo(PAGE)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalItems).isEqualTo(2)

        val firstSeries = result.items.first()
        assertThat(firstSeries).isEqualTo(
            expectedApiResponse.items[0].toEntity()
        )

        val secondSeries = result.items[1]
        assertThat(secondSeries).isEqualTo(
            expectedApiResponse.items[1].toEntity()
        )
    }

    @Test
    fun `getTopRatedTvSeries should return empty paged response when API returns empty results`() =
        runTest {
            // Given
            val emptyApiResponse = fakeEmptyApiResponse()
            coEvery { remoteDataSource.getTopRatedTvShows(PAGE) } returns Result.success(
                emptyApiResponse
            )

            coEvery { localTopRated.getAll() } returns emptyList()

            // When
            val result = repository.getTopRatedTvShows(PAGE)

            // Then
            assertThat(result.items).isEmpty()
            assertThat(result.currentPage).isEqualTo(PAGE)
            assertThat(result.totalPages).isEqualTo(1)
            assertThat(result.totalItems).isEqualTo(0)
        }

    @Test
    fun `getTopRatedTvSeries should propagate exceptions when remote call fails`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.failure(RuntimeException("Network error"))

        coEvery { localTopRated.getAll() } returns emptyList()

        // When & Then
        val ex = assertThrows<RuntimeException> { repository.getTopRatedTvShows(PAGE) }
        assertThat(ex.message).isEqualTo("Network error")
    }

    @Test
    fun `getTopRatedTvSeries should call local data source for caching`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(fakeApiResponseWithTvSeries())

        coEvery { localTopRated.getAll() } returns emptyList()

        // When
        repository.getTopRatedTvShows(PAGE)

        // Then
        coVerify { localTopRated.getAll() }
    }

    @Test
    fun `getTopRatedTvSeries should insert data to local storage after successful fetch`() =
        runTest {
            // Given
            coEvery {
                remoteDataSource.getTopRatedTvShows(PAGE)
            } returns Result.success(fakeApiResponseWithTvSeries())

            coEvery { localTopRated.getAll() } returns emptyList()
            coEvery { localTopRated.insertAll(any()) } returns Unit

            // When
            repository.getTopRatedTvShows(PAGE)

            // Then
            coVerify { localTopRated.insertAll(any()) }
        }

    @Test
    fun `getTvShowsByCategory should return data from data source if available`() = runTest {
        //Given
        coEvery {
            remoteDataSource.getTvShowsByCategoryId(any(), any())
        } returns Result.success(SearchTvShowRemoteMock)
        //When
        val result = repository.getTvShowsByCategory(
            categoryId = 1, PAGE_NUMBER
        )
        //Then
        assertThat(result).isEqualTo(TvShowList)
    }

    @Test
    fun `searchForTvShowsByCategory should throw HttpLockedException when API returns 423`() =
        runTest {
            //Given
            coEvery {
                remoteDataSource.getTvShowsByCategoryId(
                    CATEGORY_ID, PAGE_NUMBER
                )
            } returns Result.failure(NetworkException.HttpLockedException("Resource locked"))
            //When //Then
            assertThrows<NetworkException.HttpLockedException> {
                repository.getTvShowsByCategory(
                    CATEGORY_ID, PAGE_NUMBER
                )
            }
        }


    @Test
    fun `addTvShowById returns true on success`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addTvShowById returns false on failure`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `addTvEpisode returns true on success`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val episodeNumber = 2
        val rating = 9
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvShowEpisode(tvShowId, seasonNumber, episodeNumber, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addTvEpisode returns false on failure`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val episodeNumber = 2
        val rating = 9
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addTvShowEpisode(tvShowId, seasonNumber, episodeNumber, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `addTvShowById handles null guest session id`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = null

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `getPopularTvShows - when cache is empty should fetch from network and sync to cache`() =
        runTest {
            // Given
            val capturedItems = slot<List<PopularSectionLocal>>()
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
                singleTvShowResponse
            )
            coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

            // When
            val result = repository.getPopularTvShows()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(201)
            assertThat(result[0].name).isEqualTo("Test TV Show")

            coVerify(exactly = 1) { homeLocalDataSource.getAll() }
            coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
            coVerify(exactly = 1) { homeLocalDataSource.insertAll(any()) }

            // Verify sync data
            assertThat(capturedItems.captured).hasSize(1)
            assertThat(capturedItems.captured[0].mediaType).isEqualTo(MediaType.TvShow)
            assertThat(capturedItems.captured[0].id).isEqualTo(201)
        }

    @Test
    fun `getPopularTvShows - when cache has data should return cached data without network call`() =
        runTest {
            // Given
            val cachedData = listOf(
                PopularSectionLocal(
                    id = 201,
                    name = "Cached TV Show",
                    posterPictureUrl = "/cached_tv_poster.jpg",
                    rating = 9.5,
                    mediaType = MediaType.TvShow
                )
            )
            coEvery { homeLocalDataSource.getAll() } returns cachedData

            // When
            val result = repository.getPopularTvShows()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(201)
            assertThat(result[0].name).isEqualTo("Cached TV Show")

            coVerify(exactly = 1) { homeLocalDataSource.getAll() }
            coVerify(exactly = 0) { remoteDataSource.getPopularTvShows() }
            coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
        }

    @Test
    fun `getPopularTvShows - when cache has mixed media types should filter only tv shows`() =
        runTest {
            // Given
            val mixedCachedData = listOf(
                PopularSectionLocal(
                    id = 101,
                    name = "Movie",
                    posterPictureUrl = "/movie_poster.jpg",
                    rating = 8.0,
                    mediaType = MediaType.Movie
                ),
                PopularSectionLocal(
                    id = 201,
                    name = "TV Show",
                    posterPictureUrl = "/tv_poster.jpg",
                    rating = 9.0,
                    mediaType = MediaType.TvShow
                )
            )
            coEvery { homeLocalDataSource.getAll() } returns mixedCachedData

            // When
            val result = repository.getPopularTvShows()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(201)
            assertThat(result[0].name).isEqualTo("TV Show")

            coVerify(exactly = 0) { remoteDataSource.getPopularTvShows() }
        }

    @Test
    fun `getPopularTvShows - when network returns empty list should return empty list and sync empty data`() =
        runTest {
            // Given
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
                emptyTvShowResponse
            )
            coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

            // When
            val result = repository.getPopularTvShows()

            // Then
            assertThat(result).isEmpty()
            coVerify(exactly = 1) { homeLocalDataSource.insertAll(emptyList()) }
        }

    @Test
    fun `getPopularTvShows - when network returns multiple items should map and sync all items`() =
        runTest {
            // Given
            val capturedItems = slot<List<PopularSectionLocal>>()
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
                multipleTvShowsResponse
            )
            coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

            // When
            val result = repository.getPopularTvShows()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].name).isEqualTo("Test TV Show 1")
            assertThat(result[1].name).isEqualTo("Test TV Show 2")

            // Verify all items were synced
            assertThat(capturedItems.captured).hasSize(2)
            assertThat(capturedItems.captured.all { it.mediaType == MediaType.TvShow }).isTrue()
        }

    @Test
    fun `getPopularTvShows - when network throws HttpLockedException should propagate exception and log crash`() =
        runTest {
            // Given
            val exception = NetworkException.HttpLockedException("Resource locked")
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } throws exception

            // When & Then
            assertThrows<NetworkException.HttpLockedException> {
                repository.getPopularTvShows()
            }

            verify { crashReporter.logException(exception) }
            coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
        }

    @Test
    fun `getPopularTvShows - when network throws ValidationException should propagate exception and log crash`() =
        runTest {
            // Given
            val exception = NetworkException.ValidationException("Invalid data")
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } throws exception

            // When & Then
            assertThrows<NetworkException.ValidationException> {
                repository.getPopularTvShows()
            }

            verify { crashReporter.logException(exception) }
        }

    @Test
    fun `getPopularTvShows - when network returns failure result should throw exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Network error")
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.failure(exception)

            // When & Then
            assertThrows<RuntimeException> {
                repository.getPopularTvShows()
            }
        }

    @Test
    fun `getPopularTvShows - when cache has only movies should fetch from network`() = runTest {
        // Given
        val movieCacheData = listOf(
            PopularSectionLocal(
                id = 101,
                name = "Movie Only",
                posterPictureUrl = "/movie_poster.jpg",
                rating = 8.0,
                mediaType = MediaType.Movie
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns movieCacheData
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Test TV Show")

        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShows - should correctly map all tv show fields from network response`() =
        runTest {
            // Given
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
                singleTvShowResponse
            )
            coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

            // When
            val result = repository.getPopularTvShows()

            // Then
            val tvShow = result[0]
            assertThat(tvShow.id).isEqualTo(201)
            assertThat(tvShow.name).isEqualTo("Test TV Show")
            assertThat(tvShow.posterUrl).contains("/tv_poster.jpg")
            assertThat(tvShow.rating).isEqualTo(8.5)
        }

    private companion object {
        private const val TV_SHOW_ID = 1
        private const val SEASON_NUMBER = 1
        private const val EPISODE_NUMBER = 2
        private const val PAGE_NUMBER = 1
        private const val GUSET_SESSION = "mockGuestSessionId"
        private const val USER_SESSION = "mockUserSessionId"
        private const val PAGE = 1
        private const val CATEGORY_ID = 2

        private val mediaStatesDto = AccountStatesResponse(
            id = 1,
            favorite = true,
            rated = Json.parseToJsonElement("""{ "value": 7 }"""),
            watchlist = false
        )

        private val expectedEntity = MediaStates(
            id = 1, favorite = true, rate = 7, watchlist = false
        )
        val TvShowDetailsRemoteMock = TvShowDetailsRemoteResponse(
            adult = false,
            backdropPath = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            createdBy = listOf(
                TvShowCreator(
                    id = 1,
                    creditId = "credit1",
                    name = "Creator Name",
                    originalName = "Creator Original Name",
                    gender = 1,
                    profilePath = "/profile.jpg"
                )
            ),
            episodeRunTime = listOf(45, 50),
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(
                TvShowGenre(id = 1, name = "Drama")
            ),
            homepage = "https://example.com",
            id = TV_SHOW_ID,
            inProduction = true,
            languages = listOf("en", "es"),
            lastAirDate = "2023-12-31",
            lastTvShowEpisodeToAir = TvShowEpisode(
                id = 1,
                name = "Episode 1",
                overview = "Episode overview",
                voteAverage = 8.5.orZero(),
                voteCount = 100,
                airDate = "2020-01-01",
                episodeNumber = 1,
                episodeType = "standard",
                productionCode = "101",
                runtime = 45.orZero(),
                seasonNumber = 1,
                showId = TV_SHOW_ID,
                stillPath = "/still.jpg"
            ),
            name = "Test TV Show",
            nextTvShowEpisodeToAir = null,
            tvShowNetworks = listOf(
                TvShowNetwork(
                    id = 1, logoPath = "/network.jpg", name = "Network Name", originCountry = "US"
                )
            ),
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Test TV Show Original",
            overview = "Test overview",
            popularity = 85.5.orZero(),
            posterPath = "https://image.tmdb.org/t/p/w500/poster1.jpg",
            productionCompanies = listOf(
                TvShowProductionCompany(
                    id = 1,
                    logoPath = "/company.jpg",
                    name = "Production Company",
                    originCountry = "US"
                )
            ),
            productionCountries = listOf(
                TvShowProductionCountry(
                    iso31661 = "US", name = "United States"
                )
            ),
            tvShowSeasons = listOf(
                TvShowSeason(
                    airDate = "2020-01-01",
                    episodeCount = 10,
                    id = 1,
                    name = "Season 1",
                    overview = "Season overview",
                    posterPath = "/season.jpg",
                    seasonNumber = 1,
                    voteAverage = 8.0.orZero()
                )
            ),
            tvShowSpokenLanguages = listOf(
                TvShowSpokenLanguage(
                    englishName = "English", iso6391 = "en", name = "English"
                )
            ),
            status = "Returning Series",
            tagline = "Test tagline",
            type = "Scripted",
            voteAverage = 8.5.orZero(),
            voteCount = 1000
        )

        val TvShowImagesRemoteMock = TvShowImagesRemoteResponse(
            backdrops = listOf(
                ImageItem(
                    aspectRatio = 1.78,
                    height = 1080,
                    iso6391 = "en",
                    filePath = "https://image.tmdb.org/t/p/w500/backdrop1.jpg",
                    voteAverage = 8.0,
                    voteCount = 50,
                    width = 1920
                )
            ), id = TV_SHOW_ID, logos = listOf(
                ImageItem(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    filePath = "/logo1.jpg",
                    voteAverage = 7.5,
                    voteCount = 25,
                    width = 500
                )
            ), posters = listOf(
                ImageItem(
                    aspectRatio = 0.67,
                    height = 750,
                    iso6391 = "en",
                    filePath = "/poster1.jpg",
                    voteAverage = 9.0,
                    voteCount = 100,
                    width = 500
                )
            )
        )

        val TvShowEpisodesRemoteMock = TvShowEpisodesRemoteResponse(
            id = "season_id", airDate = "2020-01-01", episodes = listOf(
                TvShowEpisodeBySeason(
                    airDate = "2020-01-01",
                    episodeNumber = 1,
                    episodeType = "standard",
                    id = 1,
                    name = "Episode 1",
                    overview = "Episode overview",
                    productionCode = "101",
                    runtime = 45,
                    seasonNumber = 1,
                    showId = TV_SHOW_ID,
                    stillPath = "/still.jpg",
                    voteAverage = 8.5,
                    voteCount = 100,
                    crew = listOf(
                        EpisodeCrewMember(
                            job = "Director",
                            department = "Directing",
                            creditId = "crew1",
                            adult = false,
                            gender = 1,
                            id = 10,
                            knownForDepartment = "Directing",
                            name = "Director Name",
                            originalName = "Director Original Name",
                            popularity = 60.0,
                            profilePath = "/director.jpg"
                        )
                    ),
                    episodeGuestStars = listOf(
                        EpisodeGuestStar(
                            character = "Guest Character",
                            creditId = "guest1",
                            order = 1,
                            adult = false,
                            gender = 2,
                            id = 20,
                            knownForDepartment = "Acting",
                            name = "Guest Actor",
                            originalName = "Guest Actor Original",
                            popularity = 40.0,
                            profilePath = "/guest.jpg"
                        )
                    )
                )
            )
        )


        val EpisodeVideoResponseMock = EpisodeVideoResponse(
            id = TV_SHOW_ID, results = listOf(
                EpisodeVideoProviderRemote(
                    id = "video1",
                    key = "dQw4w9WgXcQ",
                    name = "Episode Trailer",
                    site = "YouTube",
                    type = "Trailer",
                    official = true,
                    publishedAt = "2024-01-01T00:00:00.000Z",
                    iso31661 = "US",
                    iso6391 = "en",
                    size = 1080
                ), EpisodeVideoProviderRemote(
                    id = "video2",
                    key = "abc123def456",
                    name = "Behind the Scenes",
                    site = "YouTube",
                    type = "Behind the Scenes",
                    official = false,
                    publishedAt = "2024-01-02T00:00:00.000Z",
                    iso31661 = "US",
                    iso6391 = "en",
                    size = 720
                )
            )
        )

        private fun fakeApiResponseWithTvSeries() = ApiResponse(
            currentPage = PAGE, totalPages = 1, totalItems = 2, items = listOf(
                TopRatedTvSeriesRemote(
                    adult = false,
                    backdropPath = "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
                    genreIds = listOf(18, 80),
                    id = 1396,
                    originalLanguage = "en",
                    originalName = "Breaking Bad",
                    overview = "A chemistry teacher diagnosed with cancer starts manufacturing meth.",
                    popularity = 100.0,
                    posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
                    firstAirDate = "2008-01-20",
                    name = "Breaking Bad",
                    originCountry = listOf("US"),
                    voteAverage = 8.9,
                    voteCount = 18000
                ), TopRatedTvSeriesRemote(
                    adult = false,
                    backdropPath = "/scZlQQYnDVlnpxFTxaIv2g0BWnL.jpg",
                    genreIds = listOf(18, 36),
                    id = 87108,
                    originalLanguage = "en",
                    originalName = "Chernobyl",
                    overview = "A dramatization of the true story of the Chernobyl disaster.",
                    popularity = 75.5,
                    posterPath = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
                    firstAirDate = "2019-05-06",
                    name = "Chernobyl",
                    originCountry = listOf("US", "GB"),
                    voteAverage = 9.0,
                    voteCount = 12000
                )
            )
        )

        private fun fakeEmptyApiResponse() = ApiResponse(
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 0,
            items = emptyList<TopRatedTvSeriesRemote>()
        )

        private fun createMockTrendingTvShowsApiResponse(): ApiResponse<TrendingResponse> {
            val mockTrendingItem = TrendingResponse(
                id = 1,
                name = "Test TV Show",
                posterPath = "test_poster.jpg",
                genreIds = listOf(18, 35)
            )
            return ApiResponse<TrendingResponse>(
                totalPages = 10,
                currentPage = 1,
                items = listOf(mockTrendingItem),
                totalItems = 100
            )
        }

        private fun fakeTvShowVideosResponse() = TvShowVideoResponse(
            id = 1, tvShow = listOf(
                TvShowVideoRemote(
                    id = "vid1",
                    iso31661 = "US",
                    iso6391 = "en",
                    key = "123",
                    name = "Official Trailer",
                    official = true,
                    publishedAt = "2025-07-19",
                    site = "YouTube",
                    size = 1080,
                    type = "Trailer"
                ), TvShowVideoRemote(
                    id = "vid2",
                    iso31661 = "US",
                    iso6391 = "en",
                    key = "456",
                    name = "Teaser",
                    official = false,
                    publishedAt = "2025-07-18",
                    site = "YouTube",
                    size = 720,
                    type = "Teaser"
                )
            )
        )

        private fun fakeNullTvShowVideosResponse() = TvShowVideoResponse(
            id = 999, tvShow = null
        )

        val TvShowList = PagedFetchResponse(
            PAGE_NUMBER, listOf(
                TvShow(
                    id = 2,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 10,
                    genres = listOf(),
                )
            ), totalItems = 1, totalPages = 1
        )

        private val SearchTvShowRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER, items = listOf(
                SearchTvShowRemote(
                    adult = false,
                    backdropPath = "",
                    genreIds = emptyList(),
                    id = 2,
                    originCountry = emptyList(),
                    originalLanguage = "en",
                    originalName = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    firstAirDate = "2020-07-20",
                    name = "",
                    voteAverage = 10.0,
                    voteCount = 0
                )
            ), totalPages = 1, totalItems = 1
        )

        private fun createRatingResponse() = RatingRemoteResponse(
            statusCode = 1,
            statusMessage = "Success"
        )


        val singleTvShowResponse = ApiResponse(
            currentPage = 1,
            totalItems = 50,
            totalPages = 100,
            items = listOf(
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop.jpg",
                    genreIds = listOf(5, 6),
                    id = 201,
                    originCountry = listOf("US"),
                    originalLanguage = "en",
                    originalName = "Original TV Show",
                    overview = "TV Show overview",
                    popularity = 85.0,
                    posterPath = "/tv_poster.jpg",
                    firstAirDate = "2024-03-01",
                    name = "Test TV Show",
                    voteAverage = 8.5,
                    voteCount = 1200
                )
            )
        )

        val emptyTvShowResponse = ApiResponse<PopularTvShowResponse>(
            currentPage = 1,
            totalItems = 0,
            totalPages = 0,
            items = emptyList()
        )

        val multipleTvShowsResponse = ApiResponse(
            currentPage = 1,
            totalItems = 2,
            totalPages = 1,
            items = listOf(
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop1.jpg",
                    genreIds = listOf(5, 6),
                    id = 201,
                    originCountry = listOf("US"),
                    originalLanguage = "en",
                    originalName = "Original TV Show 1",
                    overview = "TV Show overview 1",
                    popularity = 85.0,
                    posterPath = "/tv_poster1.jpg",
                    firstAirDate = "2024-03-01",
                    name = "Test TV Show 1",
                    voteAverage = 8.5,
                    voteCount = 1200
                ),
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop2.jpg",
                    genreIds = listOf(7, 8),
                    id = 202,
                    originCountry = listOf("UK"),
                    originalLanguage = "en",
                    originalName = "Original TV Show 2",
                    overview = "TV Show overview 2",
                    popularity = 75.0,
                    posterPath = "/tv_poster2.jpg",
                    firstAirDate = "2024-04-01",
                    name = "Test TV Show 2",
                    voteAverage = 9.0,
                    voteCount = 800
                )
            )
        )
    }
}
