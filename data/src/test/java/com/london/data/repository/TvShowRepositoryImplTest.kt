package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.toEntity
import com.london.data.mapper.details.tvshow.toEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodeEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodesEntity
import com.london.data.mapper.home.toprated.toEntity
import com.london.data.mapper.myrating.toEntity
import com.london.data.mapper.search.toReviewEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.ImageRemote
import com.london.data.remote.model.details.ImagesResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.GenreRemote
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowSeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeGuestStar
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
import com.london.data.remote.model.details.videoprovider.VideoTrailerRemote
import com.london.data.remote.model.home.popular.PopularTvShowResponse
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.reviews.AuthorDetailsResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.source.tvshow.TvShowRemoteDataSource
import com.london.data.repository.tvshow.TvShowRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.MediaStates
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.TvShowGenre
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
    private lateinit var homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>
    private lateinit var localTopRated: HomeLocalDataSource<TopRatedLocal>
    private lateinit var crashReporter: CrashReporter
    private lateinit var authenticationPreferences: AuthenticationPreferences

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        authenticationPreferences = mockk(relaxed = true)
        homeLocalDataSource = mockk(relaxed = true)
        localTopRated = mockk(relaxed = true)
        crashReporter = mockk(relaxed = true)

        repository = TvShowRepositoryImpl(
            tvShowRemoteDataSource = remoteDataSource,
            authenticationPreferences = authenticationPreferences,
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
        } throws NetworkException.ValidationException(
            message = "validation error",
            status = 422
        )

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowDetailsById(123)
        }
    }

    @Test
    fun `getTvShowImagesById should throw TimeoutException when remote fails`() = runTest {
        coEvery {
            remoteDataSource.getTvShowImagesById(123)
        } throws NetworkException.TimeoutException(
            message = "timeout error",
            status = 408
        )

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
        } throws NetworkException.HttpLockedException(
            message = "HttpLocked error",
            status = 423
        )

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
            } throws NetworkException.ServerErrorException(
                message = "server error",
                status = 500
            )

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
    fun `getTvShowEpisodeByPosition returns correct entity`() = runTest {
        val fakeResponse = fakeTvShowEpisodeResponse()

        coEvery {
            remoteDataSource.getEpisodeDetails(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
            )
        } returns Result.success(fakeResponse)

        val result =
            repository.getTvShowEpisodeByPosition(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEqualTo(fakeResponse.toTvShowEpisodeEntity())
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
        val mockVideoResponse = VideoResponse(
            id = TV_SHOW_ID, videos = null
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
        val mockVideoResponse = VideoResponse(
            id = TV_SHOW_ID, videos = emptyList()
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
        val networkException = NetworkException.ServerErrorException(
            message = "Server error",
            status = 500
        )

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
        } throws NetworkException.UnAuthorizedException(
            message = "401 Unauthorized",
            status = 401
        )

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
        } throws NetworkException.TimeoutException(
            message = "Request timed out",
            status = 408
        )

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
        } throws NetworkException.HttpLockedException(
            message = "Resource locked",
            status = 423
        )

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
        } throws NetworkException.ValidationException(
            message = "Invalid data",
            status = 422
        )

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
                    authorDetailsResponse = AuthorDetailsResponse(
                        authorName = "John Doe",
                        authorUsername = "johndoe",
                        authorPictureUrl = "/profile.jpg",
                        rating = 4.5
                    ),
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
    fun `getAccountTvShowStateById returns correct MediaStates`() = runTest {
        // Given
        val seriesId = 456
        coEvery {
            remoteDataSource.getAccountTvShowStates(
                tvShowId = seriesId, guestSessionId = GUSET_SESSION, userSessionId = USER_SESSION
            )
        } returns Result.success(mediaStatesDto)
        every { authenticationPreferences.getGuestSessionId() } returns GUSET_SESSION
        every { authenticationPreferences.getSessionId() } returns USER_SESSION

        // When
        val result = repository.getAccountTvShowStateById(seriesId)

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
        every { authenticationPreferences.getGuestSessionId() } returns GUSET_SESSION
        every { authenticationPreferences.getSessionId() } returns USER_SESSION

        // When
        val result = repository.getAccountTvEpisode(seriesId, seasonNumber, episodeNumber)

        // Then
        assertEquals(expectedEntity, result)
    }


    @Test
    fun `getTvShowVideos should map remote video list correctly`() = runTest {
        // Given
        val tvShowId = 123
        val seasonNumber = 1
        coEvery {
            remoteDataSource.getTvSessionVideo(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber
            )
        } returns Result.success(
            fakeTvShowVideosResponse()
        )

        // When
        val result: List<String> = repository.getTvSessionVideo(tvShowId, seasonNumber)

        // Then
        assertThat(result).hasSize(2)

        val firstVideo = result.first()
        assertThat(firstVideo).isEqualTo(fakeTvShowVideosResponse().videos?.get(0)?.youtubeKey.asYoutubeUrlOrEmpty())

        val secondVideo = result[1]
        assertThat(secondVideo).isEqualTo(
            fakeTvShowVideosResponse().videos?.get(1)?.youtubeKey.asYoutubeUrlOrEmpty()
        )
    }

    @Test
    fun `getTvShowVideos should return empty list when API returns null list`() = runTest {
        // Given
        val tvShowId = 999
        val seasonNumber = 1
        coEvery {
            remoteDataSource.getTvSessionVideo(
                tvShowId,
                seasonNumber
            )
        } returns Result.success(
            fakeNullTvShowVideosResponse()
        )

        // When
        val result = repository.getTvSessionVideo(tvShowId, seasonNumber)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowVideos should throw ValidationException when remote fails`() = runTest {

        val tvShowId = 123
        val seasonNumber = 1
        coEvery {
            remoteDataSource.getTvSessionVideo(tvShowId, seasonNumber)
        } throws NetworkException.ValidationException(
            message = "validation error",
            status = 422
        )

        assertThrows<NetworkException.ValidationException> {
            repository.getTvSessionVideo(tvShowId, seasonNumber)
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
    fun `getTopRatedTvSeries should propagate exceptions when remote call fails`() =
        runTest {
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
    fun `getTvShowsByCategory should return data from data source if available`() =
        runTest {
            //Given
            coEvery {
                remoteDataSource.getTvShowsByCategoryId(any(), any())
            } returns Result.success(SearchTvShowRemoteMock)
            //When
            val result = repository.getTvShowsByGenre(
                genre = TvShowGenre.WESTERN, PAGE_NUMBER
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
                    any(), PAGE_NUMBER
                )
            } returns Result.failure(
                NetworkException.HttpLockedException(
                    message = "Resource locked",
                    status = 423
                )
            )
            //When //Then
            assertThrows<NetworkException.HttpLockedException> {
                repository.getTvShowsByGenre(
                    TvShowGenre.WESTERN, PAGE_NUMBER
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

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
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

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
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

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
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

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
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

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
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
            val exception = NetworkException.HttpLockedException(
                message = "Resource locked",
                status = 423
            )
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
            val exception = NetworkException.ValidationException(
                message = "Invalid data",
                status = 422
            )
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
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.failure(
                exception
            )

            // When & Then
            assertThrows<RuntimeException> {
                repository.getPopularTvShows()
            }
        }

    @Test
    fun `getPopularTvShows - when cache has only movies should fetch from network`() =
        runTest {
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
            coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
                singleTvShowResponse
            )
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


    @Test
    fun `getAllRatedTvShows should return mapped RatedMedia from remote`() = runTest {
        // Given
        every { authenticationPreferences.getAccountId() } returns 123
        every { authenticationPreferences.getSessionId() } returns "fake_session"

        coEvery {
            remoteDataSource.getAllRatedTvShows(
                123,
                "fake_session"
            )
        } returns Result.success(
            fakeRatedTvResponse()
        )

        // When
        val result = repository.getAllRatedTvShows()

        // Then
        assertEquals(
            result.first(),
            fakeRatedTvResponse().items.first().toEntity(mediaType = MediaType.TvShow)
        )
        coVerify { remoteDataSource.getAllRatedTvShows(123, "fake_session") }
    }

    @Test
    fun `deleteTvShowRating should return true when remote call succeeds`() = runTest {
        // Given
        val tvShowId = 201
        every { authenticationPreferences.getSessionId() } returns "fake_session"

        coEvery {
            remoteDataSource.deleteTvShowRating(
                tvShowId,
                "fake_session"
            )
        } returns Result.success(
            fakeDeleteTvSuccessResponse()
        )

        // When
        val result = repository.deleteTvShowRating(tvShowId)

        // Then
        assertTrue(result)
        coVerify { remoteDataSource.deleteTvShowRating(tvShowId, "fake_session") }
    }

    @Test
    fun `deleteTvShowRating should return false when remote call fails`() = runTest {
        // Given
        val tvShowId = 202
        every { authenticationPreferences.getSessionId() } returns "fake_session"

        coEvery {
            remoteDataSource.deleteTvShowRating(
                tvShowId,
                "fake_session"
            )
        } returns Result.failure(
            RuntimeException("Network error")
        )

        // When
        val result = repository.deleteTvShowRating(tvShowId)

        // Then
        assertFalse(result)
        coVerify { remoteDataSource.deleteTvShowRating(tvShowId, "fake_session") }
    }

    @Test
    fun `getFirstPageTopRatedTvShows should return data from remote`() = runTest {
        // Given
        coEvery { remoteDataSource.getTopRatedTvShows(PAGE_NUMBER) } returns
                Result.success(fakeTopRatedTvSeriesRemoteResponse())

        // When
        val result = repository.getFirstPageTopRatedTvShows()

        // Then
        assertEquals(fakeTopRatedTvSeriesRemoteResponse().items.first().toEntity(), result.first())
        coVerify { remoteDataSource.getTopRatedTvShows(PAGE_NUMBER) }
        coVerify { localTopRated.insertAll(any()) }
    }

    @Test
    fun `getActorTvShowPicksById returns correct ActorMediaDetails`() = runTest {
        val fakeResponse = fakeActorTvShowDetailsResponse()

        coEvery { remoteDataSource.getActorTvShowById(101) } returns Result.success(fakeResponse)

        val result = repository.getActorTvShowPicksById(101)

        assertThat(result).isEqualTo(fakeResponse.toEntity())
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

        fun fakeTvShowEpisodeResponse(
            id: Int = 1,
            seasonNumber: Int = 1,
            name: String = "Episode 1",
            overview: String = "Episode overview",
            airDate: String = "2025-01-01",
            stillPath: String? = "/still.jpg",
            voteAverage: Double = 8.5,
            voteCount: Int = 100,
            guestStars: List<EpisodeGuestStar> = emptyList(),
            episodeType: String = "standard"
        ): TvShowEpisodeResponse = TvShowEpisodeResponse(
            id = id,
            name = name,
            seasonNumber = seasonNumber,
            episodeType = episodeType,
            airDate = airDate,
            overview = overview,
            stillPath = stillPath,
            voteAverage = voteAverage,
            voteCount = voteCount,
            guestStars = guestStars
        )

        fun fakeTopRatedTvSeriesRemoteResponse(
            items: List<TopRatedTvSeriesRemote> = listOf(
                TopRatedTvSeriesRemote(
                    id = 301,
                    name = "Fake Top Rated TV Show",
                    posterPath = "/fake_tv_poster.jpg",
                    firstAirDate = "2025-01-01",
                    genreIds = listOf(1, 2, 3),
                    voteAverage = 9.5
                )
            )
        ): ApiResponse<TopRatedTvSeriesRemote> =
            ApiResponse(
                currentPage = 1,
                totalPages = 5,
                totalItems = 100,
                items = items
            )

        private fun fakeRatedTvResponse() = ApiResponse(
            currentPage = 1,
            totalPages = 1,
            totalItems = 2,
            items = listOf(
                RatingMediaResponse(
                    id = 301,
                    title = "Rated TV Show 1",
                    posterPath = "/tv_poster1.jpg",
                    rating = 9.0
                ),
                RatingMediaResponse(
                    id = 302,
                    title = "Rated TV Show 2",
                    posterPath = "/tv_poster2.jpg",
                    rating = 8.5
                )
            )
        )

        private fun fakeDeleteTvSuccessResponse() = RatingRemoteResponse(
            statusCode = 1,
            statusMessage = "Deleted successfully",
            success = true
        )

        fun fakeActorTvShowDetailsResponse(
            actorId: Int = 101,
            cast: List<ActorTvShowCastMember> = listOf(
                ActorTvShowCastMember(id = 1, posterPath = "/poster1.jpg"),
                ActorTvShowCastMember(id = 2, posterPath = "/poster2.jpg")
            )
        ): ActorTvShowDetailsResponse = ActorTvShowDetailsResponse(
            id = actorId,
            cast = cast
        )
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
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(
                GenreRemote(id = 1, name = "Drama")
            ),
            id = TV_SHOW_ID,
            name = "Test TV Show",
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            overview = "Test overview",
            posterPath = "https://image.tmdb.org/t/p/w500/poster1.jpg",
            tvShowSeasons = listOf(
                TvShowSeason(
                    seasonNumber = 1,
                )
            ),
            voteAverage = 8.5.orZero(),
        )

        val TvShowImagesRemoteMock = ImagesResponse(
            backdrops = listOf(
                ImageRemote(
                    filePath = "https://image.tmdb.org/t/p/w500/backdrop1.jpg",
                )
            ), id = TV_SHOW_ID, logos = listOf(
                ImageRemote(
                    filePath = "/logo1.jpg",
                )
            ), posters = listOf(
                ImageRemote(
                    filePath = "/poster1.jpg",
                )
            )
        )

        val TvShowEpisodesRemoteMock = TvShowEpisodesRemoteResponse(
            id = "season_id",
            episodes = listOf(
                TvShowEpisodeBySeason(
                    airDate = "2020-01-01",
                    episodeNumber = 1,
                    episodeType = "standard",
                    id = 1,
                    name = "Episode 1",
                    overview = "Episode overview",
                    runtime = 45,
                    seasonNumber = 1,
                    showId = TV_SHOW_ID,
                    stillPath = "/still.jpg",
                    voteAverage = 8.5,
                )
            )
        )


        val EpisodeVideoResponseMock = VideoResponse(
            id = TV_SHOW_ID,
            videos = listOf(
                VideoTrailerRemote(
                    youtubeKey = "dQw4w9WgXcQ",
                ), VideoTrailerRemote(
                    youtubeKey = "abc123def456",
                )
            )
        )

        private fun fakeApiResponseWithTvSeries() = ApiResponse(
            currentPage = PAGE, totalPages = 1, totalItems = 2, items = listOf(
                TopRatedTvSeriesRemote(
                    genreIds = listOf(18, 80),
                    id = 1396,
                    posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
                    firstAirDate = "2008-01-20",
                    name = "Breaking Bad",
                    voteAverage = 8.9,
                ), TopRatedTvSeriesRemote(
                    genreIds = listOf(18, 36),
                    id = 87108,
                    posterPath = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
                    firstAirDate = "2019-05-06",
                    name = "Chernobyl",
                    voteAverage = 9.0,
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

        private fun fakeTvShowVideosResponse() = VideoResponse(
            id = 1, videos = listOf(
                VideoTrailerRemote(
                    youtubeKey = "123",
                ), VideoTrailerRemote(
                    youtubeKey = "456",
                )
            )
        )

        private fun fakeNullTvShowVideosResponse() = VideoResponse(
            id = 999, videos = null
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
                    genreIds = emptyList(),
                    id = 2,
                    posterPath = "",
                    firstAirDate = "2020-07-20",
                    name = "",
                    voteAverage = 10.0,
                )
            ),
            totalPages = 1,
            totalItems = 1
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
                    id = 201,
                    posterPath = "/tv_poster.jpg",
                    name = "Test TV Show",
                    voteAverage = 8.5,
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
                    id = 201,
                    posterPath = "/tv_poster1.jpg",
                    name = "Test TV Show 1",
                    voteAverage = 8.5,
                ),
                PopularTvShowResponse(
                    id = 202,
                    posterPath = "/tv_poster2.jpg",
                    name = "Test TV Show 2",
                    voteAverage = 9.0,
                )
            )
        )
    }
}
