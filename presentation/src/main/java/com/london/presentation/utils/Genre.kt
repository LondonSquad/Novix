package com.london.presentation.utils

import androidx.annotation.StringRes
import com.london.presentation.R

data class GenreId(
    @StringRes val name: Int,
    val movieId: Int,
    val tvShowId: Int,
)

enum class Genre(val id: GenreId) {
    All(id = GenreId(name = R.string.all, movieId = 0, tvShowId = 0)),
    Action(id = GenreId(name = R.string.action, movieId = 28, tvShowId = 10759)),
    Adventure(id = GenreId(name = R.string.adventure, movieId = 12, tvShowId = 10759)),
    Animation(id = GenreId(name = R.string.animation, movieId = 16, tvShowId = 16)),
    Comedy(id = GenreId(name = R.string.comedy, movieId = 35, tvShowId = 35)),
    Crime(id = GenreId(name = R.string.crime, movieId = 80, tvShowId = 80)),
    Documentary(id = GenreId(name = R.string.documentary, movieId = 99, tvShowId = 99)),
    Drama(id = GenreId(name = R.string.drama, movieId = 18, tvShowId = 18)),
    Family(id = GenreId(name = R.string.family, movieId = 10751, tvShowId = 10751)),
    Fantasy(id = GenreId(name = R.string.fantasy, movieId = 14, tvShowId = 10765)),
    History(id = GenreId(name = R.string.history, movieId = 36, tvShowId = 0)),
    Horror(id = GenreId(name = R.string.horror, movieId = 27, tvShowId = 0)),
    Music(id = GenreId(name = R.string.music, movieId = 10402, tvShowId = 0)),
    Mystery(id = GenreId(name = R.string.mystery, movieId = 9648, tvShowId = 9648)),
    Romance(id = GenreId(name = R.string.romance, movieId = 10749, tvShowId = 0)),
    SciFi(id = GenreId(name = R.string.sci_fi, movieId = 878, tvShowId = 10765)),
    TvMovie(id = GenreId(name = R.string.tv_movie, movieId = 10770, tvShowId = 0)),
    Thriller(id = GenreId(name = R.string.thriller, movieId = 53, tvShowId = 0)),
    War(id = GenreId(name = R.string.war, movieId = 10752, tvShowId = 10768)),
    Western(id = GenreId(name = R.string.western, movieId = 37, tvShowId = 37)),
    Kids(id = GenreId(name = R.string.kids, movieId = 0, tvShowId = 10762)),
    News(id = GenreId(name = R.string.news, movieId = 0, tvShowId = 10763)),
    Reality(id = GenreId(name = R.string.reality, movieId = 0, tvShowId = 10764)),
    Soap(id = GenreId(name = R.string.soap, movieId = 0, tvShowId = 10766)),
    Talk(id = GenreId(name = R.string.talk, movieId = 0, tvShowId = 10767)),
    Politics(id = GenreId(name = R.string.politics, movieId = 0, tvShowId = 10768)),
}
