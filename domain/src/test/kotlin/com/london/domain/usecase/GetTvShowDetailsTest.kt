package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshowdetails.TvShowCreatorEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowEpisodeEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.entity.tvshowdetails.TvShowNetworkEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCompanyEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCountryEntity
import com.london.domain.entity.tvshowdetails.TvShowSeasonEntity
import com.london.domain.entity.tvshowdetails.TvShowSpokenLanguageEntity
import com.london.domain.error.TvShowDetailsSearchFailedException
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowDetailsTest {
    lateinit var tvShowRepository: TvShowRepository
    lateinit var getTvShowDetails: GetTvShowDetails

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        getTvShowDetails = GetTvShowDetails(tvShowRepository)
    }

    @Test
    fun `should return tv show details when repository returns tv show details`() = runTest {
        //given
        coEvery { tvShowRepository.getTvShowDetailsById(TV_SHOW_ID) } returns mockTvShowDetails
        //when
        val result = getTvShowDetails(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockTvShowDetails)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        //given
        coEvery { tvShowRepository.getTvShowDetailsById(TV_SHOW_ID) } throws TvShowDetailsSearchFailedException()
        //when //then
        assertThrows<TvShowDetailsSearchFailedException> {
            getTvShowDetails(TV_SHOW_ID)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        val mockTvShowDetails = TvShowDetailsEntity(
            adult = false,
            backdropUrl = "/backdrop.jpg",
            createdBy = listOf(
                TvShowCreatorEntity(
                    id = 1,
                    creditId = "credit1",
                    name = "Creator Name",
                    originalName = "Creator Name",
                    gender = 1,
                    profileUrl = "/profile.jpg"
                )
            ),
            episodeRunTime = listOf(45),
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(TvShowGenreEntity(id = 1, name = "Drama")),
            homepage = "https://example.com",
            id = TV_SHOW_ID,
            inProduction = true,
            languages = listOf("en"),
            lastAirDate = "2023-12-31",
            lastTvShowEpisodeToAir = TvShowEpisodeEntity(
                id = 1,
                name = "Episode 1",
                overview = "Overview",
                voteAverage = 8.5,
                voteCount = 100,
                airDate = "2023-12-31",
                episodeNumber = 1,
                episodeType = "finale",
                productionCode = "P001",
                runtime = 45,
                seasonNumber = 1,
                showId = TV_SHOW_ID,
                stillPath = "/still.jpg"
            ),
            name = "Test Show",
            nextTvShowEpisodeToAir = null,
            tvShowNetworks = listOf(
                TvShowNetworkEntity(
                    id = 1,
                    logoUrl = "/network.jpg",
                    name = "Network",
                    originCountry = "US"
                )
            ),
            numberOfEpisodes = 10,
            numberOfSeasons = 2,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Test Show",
            overview = "A test show overview",
            popularity = 85.5,
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
                    iso31661 = "US",
                    name = "United States"
                )
            ),
            tvShowSeasons = listOf(
                TvShowSeasonEntity(
                    airDate = "2020-01-01",
                    episodeCount = 10,
                    id = 1,
                    name = "Season 1",
                    overview = "Season 1 overview",
                    posterUrl = "/season1.jpg",
                    seasonNumber = 1,
                    voteAverage = 8.0
                )
            ),
            tvShowSpokenLanguageEntities = listOf(
                TvShowSpokenLanguageEntity(
                    englishName = "English",
                    iso6391 = "en",
                    name = "English"
                )
            ),
            status = "Ended",
            tagline = "Test tagline",
            type = "Scripted",
            voteAverage = 8.5,
            voteCount = 1000
        )
    }
}