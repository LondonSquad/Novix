package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.ApiResponse
import com.london.data.mapper.toAuthorDetails
import com.london.data.mapper.toReviewEntity
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.data.mapper.tvshowdetails.toTvShowEpisodesEntity
import com.london.data.remote.exception.NetworkException
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
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.model.reviews.model.AuthorDetailsResponse
import com.london.data.remote.model.reviews.model.ReviewResponse
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowCreatorEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowEpisodeEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.TvShowNetworkEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCompanyEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCountryEntity
import com.london.domain.entity.tvshowdetails.TvShowRoleEntity
import com.london.domain.entity.tvshowdetails.TvShowSeasonEntity
import com.london.domain.entity.tvshowdetails.TvShowSpokenLanguageEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodeCrewMemberEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodeGuestStarEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class DetailsRepositoryImplTest {

    private lateinit var tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource
    private lateinit var repository: DetailsRepositoryImpl
    private lateinit var reviewsRemoteDataSource: ReviewsRemoteDataSource

    @Before
    fun setUp() {
        tvShowDetailsRemoteDataSource = mockk(relaxed = true)
        reviewsRemoteDataSource = mockk(relaxed = true)
        repository = DetailsRepositoryImpl(
            tvShowDetailsRemoteDataSource, reviewsRemoteDataSource = reviewsRemoteDataSource
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
    fun `getMovieReviews should return paged reviews when remote succeeds`() = runTest {
        // Given
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
                        authorPictureUrl = "https://image.tmdb.org/t/p/w500/profile.jpg",
                        rating = 4.5
                    ),
                    url = "https://example.com/review1"
                )
            ), totalPages = 1, totalItems = 1
        )
        coEvery { reviewsRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.returns(
            Result.success(fakeRemoteResponse)
        )

        // When
        val result: PagedFetchResponse<ReviewEntity> =
            repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)

        //Then
        assertThat(result.items.first().authorDetails).isEqualTo(fakeRemoteResponse.items.first().authorDetailsResponse.toAuthorDetails())
    }

    @Test
    fun `getMovieReviews should throw when remote fails`() = runTest {
        //Given
        coEvery { reviewsRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.throws(
            RuntimeException("Network error")
        )

        // When && Then
        val exception = assertThrows<RuntimeException> {
            repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)
        }
        assertThat(exception.message).contains("Network error")
    }

    @Test
    fun `getMovieReviews should return empty when remote has no results`() = runTest {
        //Given
        val fakeEmptyResponse = ApiResponse(
            currentPage = 1, items = emptyList<ReviewResponse>(), totalPages = 0, totalItems = 0
        )

        coEvery { reviewsRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.returns(
            Result.success(fakeEmptyResponse)
        )

        // When
        val result = repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)

        // Then
        assertThat(result.items).isEmpty()
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

    private companion object {
        const val TV_SHOW_ID = 1
        const val SEASON_NUMBER = 1
        const val EPISODE_NUMBER = 2
        const val MOVIE_ID = 99
        const val PAGE_NUMBER = 1

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

        val TvShowDetailsEntityMock = TvShowDetailsEntity(
            adult = false,
            backdropUrl = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            createdBy = listOf(
                TvShowCreatorEntity(
                    id = 1,
                    creditId = "credit1",
                    name = "Creator Name",
                    originalName = "Creator Original Name",
                    gender = 1,
                    profileUrl = "/profile.jpg"
                )
            ),
            episodeRunTime = listOf(45, 50),
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(
                TvShowGenreEntity(id = 1, name = "Drama")
            ),
            homepage = "https://example.com",
            id = TV_SHOW_ID,
            inProduction = true,
            languages = listOf("en", "es"),
            lastAirDate = "2023-12-31",
            lastTvShowEpisodeToAir = TvShowEpisodeEntity(
                id = 1,
                name = "Episode 1",
                overview = "Episode overview",
                voteAverage = 8.5,
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
                TvShowNetworkEntity(
                    id = 1,
                    logoUrl = "/network.jpg".asImageUrlOrEmpty(),
                    name = "Network Name",
                    originCountry = "US"
                )
            ),
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Test TV Show Original",
            overview = "Test overview",
            popularity = 85.5.orZero(),
            posterUrl = "/poster.jpg",
            productionCompanies = listOf(
                TvShowProductionCompanyEntity(
                    id = 1,
                    logoUrl = "/company.jpg",
                    name = "Production Company",
                    originCountry = "US"
                )
            ),
            productionCountries = listOf(
                TvShowProductionCountryEntity(
                    iso31661 = "US", name = "United States"
                )
            ),
            tvShowSeasons = listOf(
                TvShowSeasonEntity(
                    airDate = "2020-01-01",
                    episodeCount = 10,
                    id = 1,
                    name = "Season 1",
                    overview = "Season overview",
                    posterUrl = "/season.jpg",
                    seasonNumber = 1,
                    voteAverage = 8.0
                )
            ),
            tvShowSpokenLanguageEntities = listOf(
                TvShowSpokenLanguageEntity(
                    englishName = "English", iso6391 = "en", name = "English"
                )
            ),
            status = "Returning Series",
            tagline = "Test tagline",
            type = "Scripted",
            voteAverage = 8.5,
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

        val TvShowCastEntityMock = TvShowCastEntity(
            cast = listOf(
                TvShowCastMemberEntity(
                    adult = false,
                    gender = 2,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "Actor Name",
                    originalName = "Actor Original Name",
                    popularity = 75.5,
                    profileUrl = "/actor.jpg",
                    roles = listOf(
                        TvShowRoleEntity(
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

        val TvShowImagesEntityMock = TvShowImagesEntity(
            backdrops = listOf(
                ImageItemEntity(
                    aspectRatio = 1.78,
                    height = 1080,
                    iso6391 = "en",
                    fileUrl = "https://image.tmdb.org/t/p/w500/backdrop1.jpg",
                    voteAverage = 8.0,
                    voteCount = 50,
                    width = 1920
                )
            ), id = TV_SHOW_ID, logos = listOf(
                ImageItemEntity(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    fileUrl = "https://image.tmdb.org/t/p/w500/logo1.jpg",
                    voteAverage = 7.5,
                    voteCount = 25,
                    width = 500
                )
            ), posters = listOf(
                ImageItemEntity(
                    aspectRatio = 0.67,
                    height = 750,
                    iso6391 = "en",
                    fileUrl = "https://image.tmdb.org/t/p/w500/poster1.jpg",
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

        val TvShowEpisodesEntityMock = TvShowEpisodesEntity(
            id = "season_id", airDate = "2020-01-01", episodes = listOf(
                TvShowEpisodeBySeasonEntity(
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
                    stillUrl = "/still.jpg",
                    voteAverage = 8.5,
                    voteCount = 100,
                    crew = listOf(
                        EpisodeCrewMemberEntity(
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
                        EpisodeGuestStarEntity(
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
    }
}