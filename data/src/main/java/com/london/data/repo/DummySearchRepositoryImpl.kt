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
                name = "Interstellar",
                posterPicture = "https://encrypted-tbn0.gstatic.com/" +
                        "images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6" +
                        "rUg-CtflZj07HfbT8d2GwKWg"
            ),
            Movie(
                id = 2,
                name = "The Dark Knight",
                posterPicture = "https://www.google.com/url?s" +
                        "a=i&url=https%3A%2F%2Fencrypted-tbn3.gstatic.c" +
                        "om%2Fimages%3Fq%3Dtbn%3AANd9GcQkUywIUXDjHSQJI" +
                        "aNHYVs08osgBpF5Ot-xmB_omyEZeeRP9Xug&psig=AOv" +
                        "Vaw3wEbftfPGLsk7OTe20N4np&ust=1752323246254000&source=im" +
                        "ages&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCNCChPfmtI4DFQAAAAAdAAAAABAE"
            ),
            Movie(
                id = 3,
                name = "Inception",
                posterPicture = "https://encrypted-tbn3.gstatic.com/images?q=tbn" +
                        ":ANd9GcQovCe0H45fWwAtV31ajOdXRPTxSsMQgPIQ3lcZX_mAW0jXV3kH"
            ),
            Movie(
                id = 4,
                name = "Dune",
                posterPicture = "https://www.google.com/url?sa=i&u" +
                        "rl=https%3A%2F%2Fencrypted-tbn2.gstatic.com%2Fimages%3Fq%3D" +
                        "tbn%3AANd9GcTzGMepFMvymqy06LF-NsSpgYxeujNWwbXto-bc868K2bl8-zu6&psig=AOvV" +
                        "aw1wUr0YHiRlDCcfaE54gXib&ust=1752323304176000&source" +
                        "=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCIjtvpLntI4DFQAAAAAdAAAAABAE"
            ),
        )
    }

    override suspend fun searchForTvShows(
        name: String, language: String
    ): List<TvShow> {
        return listOf(
            TvShow(
                id = 1,
                name = "Breaking bad",
                posterPicture = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBmR1UfShiKt6vY3J9tjztfpJvB7qM3xIQ_-TUF25_zZYzoTfz"
            ),
            TvShow(
                id = 2,
                name = "Game of thrones",
                posterPicture = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn0.gstatic.com%2Fimages%3Fq%3Dtbn%3AANd9GcS_C1zvb87cxPL28JSRzOFw1SAHMMKARQ2fswLBb6L17zy-f9h7&psig=AOvVaw0g92hp3wVTkHgZsu8VwcWE&ust=1752323370945000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCPD1sbLntI4DFQAAAAAdAAAAABAE"
            ),
            TvShow(
                id = 3,
                name = "The office",
                posterPicture = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Fimages%3Fq%3Dtbn%3AANd9GcT1rLm86VJA7Tcf4ZjWQE6FrXCh9lvUu3RzeNIEDH2YqD3ta8BG&psig=AOvVaw1PDk53tugwxyA40JsQLbW6&ust=1752323390435000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCIif2rvntI4DFQAAAAAdAAAAABAE"
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