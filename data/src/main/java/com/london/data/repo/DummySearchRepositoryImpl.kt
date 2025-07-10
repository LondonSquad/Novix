package com.london.data.repo

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repo.SearchRepository

class DummySearchRepositoryImpl : SearchRepository {
    override suspend fun searchForMovies(
        name: String, language: String
    ): List<Movie> {
        return listOf(
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

    override suspend fun searchForTvShows(
        name: String, language: String
    ): List<TvShow> {
        return listOf(
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
    }

    override suspend fun searchForActors(
        name: String, language: String
    ): List<Actor> {
        return listOf(
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
    }
}