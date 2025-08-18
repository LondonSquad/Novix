package com.london.domain.usecase.search

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ManageSearchUseCaseTest {
    private lateinit var repository: SearchRepository
    private lateinit var manageSearchUseCase: ManageSearchUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        manageSearchUseCase = ManageSearchUseCase(repository)
    }

    @Test
    fun `should return PagedFetchResponse when SearchRepository returns PagedFetchResponse`() =
        runTest {
            //given
            coEvery {
                repository.searchForActors(
                    TvSHOW_NAME,
                    PAGE_NUMBER
                )
            } returns actorsPagedResponse
            //when
            val result = manageSearchUseCase.searchForActors(TvSHOW_NAME, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(actorsPagedResponse)
        }

    @Test
    fun `invoke should return list of genre interest counts`(): Unit = runTest {
        val mediaType = "movie"
        val expected = listOf(1 to 5, 2 to 3)

        coEvery { repository.getGenreInterestCounts(mediaType) } returns expected

        val result = manageSearchUseCase.getGenreInterestCounts(mediaType)

        assertThat(result).isEqualTo(expected)
        coVerify { repository.getGenreInterestCounts(mediaType) }
    }

    @Test
    fun `invoke should call repository to increment genre interest`() = runTest {
        val mediaType = "tv"

        manageSearchUseCase.incrementGenreInterest(MovieGenre.ACTION, mediaType)

        coVerify { repository.incrementGenreInterest(MovieGenre.ACTION, mediaType) }
    }

    @Test
    fun `should return a paged fetch response of movies when repository successfully fetches movies`() =
        runTest {
            //given
            coEvery {
                repository.searchForMovies(
                    MOVIE_NAME,
                    PAGE_NUMBER
                )
            } returns moviesPagedResponse
            //when
            val result =
                manageSearchUseCase.searchForMovies(name = MOVIE_NAME, pageNumber = PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(moviesPagedResponse)
        }

    @Test
    fun `searchForTvShows should return a paged fetch response of tv shows when repository successfully fetches tv shows`() =
        runTest {
            //given
            coEvery {
                repository.searchForTvShows(
                    TvSHOW_NAME,
                    PAGE_NUMBER
                )
            } returns tvShowsPagedResponse()
            //when
            val result = manageSearchUseCase.searchForTvShows(TvSHOW_NAME, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(tvShowsPagedResponse())
        }

    private companion object {
        private const val MOVIE_NAME = "Movie"
        private const val TvSHOW_NAME = "Tom"
        private const val PAGE_NUMBER = 1

        private fun tvShowsPagedResponse(): PagedFetchResponse<TvShow> = PagedFetchResponse(
            currentPage = 1,
            items = listOf(tvShow),
            totalPages = 1,
            totalItems = 1
        )

        private val tvShow = TvShow(
            id = 1,
            name = TvSHOW_NAME,
            releaseYear = 2024,
            rating = 8,
            genres = listOf(TvShowGenre.ALL),
            posterPicture = ""
        )
        private val ACTOR = Actor(
            id = 1,
            name = "Tom Holland",
            profilePictureUrl = "",
            characterName = ""
        )
        private val actorsPagedResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(ACTOR),
            totalPages = 1,
            totalItems = 1
        )
        private val movie = Movie(
            id = 1,
            name = MOVIE_NAME,
            posterUrl = "",
            releaseYear = 2024,
            rating = 8,
            genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
        )
        val moviesPagedResponse: PagedFetchResponse<Movie> = PagedFetchResponse(
            currentPage = 1,
            items = listOf(movie),
            totalPages = 1,
            totalItems = 1
        )
    }
}