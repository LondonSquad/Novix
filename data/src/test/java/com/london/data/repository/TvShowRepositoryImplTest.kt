package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.details.tvshow.TvShowImagesMapper.toEntity
import com.london.data.mapper.details.tvshow.toCastEntity
import com.london.data.mapper.details.tvshow.toEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodesEntity
import com.london.data.mapper.search.toReviewEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.tvshow.model.ImageItem
import com.london.data.remote.model.details.tvshow.model.Role
import com.london.data.remote.model.details.tvshow.model.TvShowCastMember
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
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
import com.london.data.remote.model.reviews.AuthorDetailsResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.repository.search.TvShowRepositoryImpl
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.moviedatails.MediaStates
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TvShowRepositoryImplTest {

    private lateinit var tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource
    private lateinit var repository: TvShowRepositoryImpl
    private lateinit var reviewsRemoteDataSource: ReviewsRemoteDataSource
    private lateinit var authPreferences: AuthPreferences

    @Before
    fun setUp() {
        tvShowDetailsRemoteDataSource = mockk(relaxed = true)
        reviewsRemoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = false)
        repository = TvShowRepositoryImpl(
            tvShowDetailsRemoteDataSource,
            reviewsRemoteDataSource = reviewsRemoteDataSource,
            authPreferences = authPreferences,
        )
    }

    @Test
    fun `getTvShowDetailsById should return TvShowDetailsEntity when remote call succeeds`() =
        runTest {
            coEvery { tvShowDetailsRemoteDataSource.getTvShowDetailsById(TV_SHOW_ID) }.returns(
                Result.success(TvShowDetailsRemoteMock)
            )

            val result = repository.getTvShowDetailsById(TV_SHOW_ID)

            assertThat(result).isEqualTo(TvShowDetailsRemoteMock.toEntity())
        }

    @Test
    fun `getCastTvShowById should return TvShowCastEntity when remote call succeeds`() = runTest {
        coEvery { tvShowDetailsRemoteDataSource.getCastsByTvShowId(TV_SHOW_ID) }.returns(
            Result.success(
                TvShowCastRemoteMock
            )
        )

        val result = repository.getCastTvShowById(TV_SHOW_ID)

        assertThat(result).isEqualTo(TvShowCastRemoteMock.toCastEntity())
    }

    @Test
    fun `getImagesTvShowById should return TvShowImagesEntity when remote call succeeds`() =
        runTest {
            coEvery { tvShowDetailsRemoteDataSource.getTvShowImagesById(TV_SHOW_ID) }.returns(
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
            tvShowDetailsRemoteDataSource.getTvShowDetailsById(123)
        } throws NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowDetailsById(123)
        }
    }

    @Test
    fun `getCastsByTvShowId should throw UnAuthorizedException when remote fails`() = runTest {
        coEvery {
            tvShowDetailsRemoteDataSource.getCastsByTvShowId(123)
        } throws NetworkException.UnAuthorizedException("unAuthorized error")

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getCastTvShowById(123)
        }
    }

    @Test
    fun `getTvShowImagesById should throw TimeoutException when remote fails`() = runTest {
        coEvery {
            tvShowDetailsRemoteDataSource.getTvShowImagesById(123)
        } throws NetworkException.TimeoutException("timeout error")

        assertThrows<NetworkException.TimeoutException> {
            repository.getImagesTvShowById(123)
        }
    }

    @Test
    fun `getTvShowEpisodesBySeason should throw HttpLockedException when remote fails`() = runTest {
        coEvery {
            tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(
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
                tvShowDetailsRemoteDataSource.getEpisodeDetailsByPosition(
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
                tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)
            }.returns(Result.success(TvShowEpisodesRemoteMock))

            val result = repository.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)

            assertThat(result).isEqualTo(TvShowEpisodesRemoteMock.toTvShowEpisodesEntity())
        }

    @Test
    fun `getTvShowEpisodesBySeason should throw original exception when remote call fails`() =
        runTest {
            val networkException = RuntimeException("Network error")
            coEvery {
                tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(TV_SHOW_ID, SEASON_NUMBER)
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
                tvShowDetailsRemoteDataSource.getEpisodeDetailsByPosition(
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
                tvShowDetailsRemoteDataSource.getEpisodeVideos(
                    tvShowId = TV_SHOW_ID,
                    seasonNumber = SEASON_NUMBER,
                    episodeNumber = EPISODE_NUMBER
                )
            }.returns(Result.success(EpisodeVideoResponseMock))

            // When
            val result = repository.getEpisodeVideos(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

            // Then
            val expectedUrls = listOf(
                "dQw4w9WgXcQ".asYoutubeUrlOrEmpty(),
                "abc123def456".asYoutubeUrlOrEmpty()
            )
            assertThat(result).isEqualTo(expectedUrls)
        }

    @Test
    fun `getEpisodeVideos should return empty list when remote returns null results`() = runTest {
        // Given
        val mockVideoResponse = EpisodeVideoResponse(
            id = TV_SHOW_ID,
            results = null
        )

        coEvery {
            tvShowDetailsRemoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
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
            id = TV_SHOW_ID,
            results = emptyList()
        )

        coEvery {
            tvShowDetailsRemoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
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
            tvShowDetailsRemoteDataSource.getEpisodeVideos(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
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
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, page)
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
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, page)
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
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, page)
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
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, page)
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

        coEvery { reviewsRemoteDataSource.getTvShowReviews(TV_SHOW_ID, PAGE_NUMBER) }.returns(
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
            tvShowDetailsRemoteDataSource.getAccountTvShowStates(
                tvShowId = seriesId,
                guestSessionId = GUSET_SESSION,
                userSessionId = USER_SESSION
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
            tvShowDetailsRemoteDataSource.getAccountTvEpisodeState(
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
        coEvery { tvShowDetailsRemoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
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
        coEvery { tvShowDetailsRemoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
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
            tvShowDetailsRemoteDataSource.getTvShowVideos(tvShowId)
        } throws NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowVideos(tvShowId)
        }
    }


    private fun fakeTvShowVideosResponse() = TvShowVideoResponse(
        id = 1,
        tvShow = listOf(
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
            ),
            TvShowVideoRemote(
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
        id = 999,
        tvShow = null
    )

    private companion object {
        private const val TV_SHOW_ID = 1
        private const val SEASON_NUMBER = 1
        private const val EPISODE_NUMBER = 2
        private const val PAGE_NUMBER = 1
        private const val GUSET_SESSION = "mockGuestSessionId"
        private const val USER_SESSION = "mockUserSessionId"
        private val mediaStatesDto = AccountStatesResponse(
            id = 1,
            favorite = true,
            rated = Json.parseToJsonElement("""{ "value": 7 }"""),
            watchlist = false
        )

        private val expectedEntity = MediaStates(
            id = 1,
            favorite = true,
            rate = 7,
            watchlist = false
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

        val TvShowCastRemoteMock = TvShowCastRemoteResponse(
            cast = listOf(
                TvShowCastMember(
                    adult = false,
                    gender = 2,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "Actor Name",
                    originalName = "Actor Original Name",
                    popularity = 75.5,
                    profilePath = "/actor.jpg",
                    roles = listOf(
                        Role(
                            creditId = "role1", character = "Main Character", episodeCount = 10
                        )
                    ),
                    totalEpisodeCount = 10,
                    order = 1
                )
            ), id = TV_SHOW_ID
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
            id = TV_SHOW_ID,
            results = listOf(
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
                ),
                EpisodeVideoProviderRemote(
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
    }
}
