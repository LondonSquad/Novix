package com.london.data.repo

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DummySearchRepositoryImplTest {
    private lateinit var dummySearchRepositoryImpl: DummySearchRepositoryImpl

    @Before
    fun setUp() {
        dummySearchRepositoryImpl = DummySearchRepositoryImpl()
    }

    @Test
    fun `searchForMovies should return a list of movies`() = runTest {
        val result = dummySearchRepositoryImpl.searchForMovies(NAME, LANGUAGE)
        assertThat(result).isEqualTo(MOVIES)
    }

    @Test
    fun `searchForTvShows should return a list of tv shows`() = runTest {
        val result = dummySearchRepositoryImpl.searchForTvShows(NAME, LANGUAGE)
        assertThat(result).isEqualTo(TV_SHOWS)
    }

    @Test
    fun `searchForActors should return a list of actors`() = runTest {
        val result = dummySearchRepositoryImpl.searchForActors(NAME, LANGUAGE)
        assertThat(result).isEqualTo(ActorS)
    }

    private companion object {
        const val NAME = "Tom"
        const val LANGUAGE = "en-US"
        val ActorS = listOf(
            Actor(
                id = 1,
                name = "Actor 1",
                profilePicture = ""
            ),
            Actor(
                id = 2,
                name = "Actor 2",
                profilePicture = ""
            ),
            Actor(
                id = 3,
                name = "Actor 3",
                profilePicture = ""
            ),
            Actor(
                id = 4,
                name = "Actor 4",
                profilePicture = ""
            ),
        )
        val TV_SHOWS = listOf(
            TvShow(
                id = 1,
                posterPicture = ""
            ),
            TvShow(
                id = 2,
                posterPicture = ""
            ),
            TvShow(
                id = 3,
                posterPicture = ""
            ),
            TvShow(
                id = 4,
                posterPicture = ""
            ),
        )
        val MOVIES = listOf(
            Movie(
                id = 1,
                posterPicture = ""
            ),
            Movie(
                id = 2,
                posterPicture = ""
            ),
            Movie(
                id = 3,
                posterPicture = ""
            ),
            Movie(
                id = 4,
                posterPicture = ""
            ),
        )
    }
}