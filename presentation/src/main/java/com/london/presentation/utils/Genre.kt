package com.london.presentation.utils

import androidx.annotation.StringRes
import com.london.presentation.R

enum class Genre(val id: Int, @StringRes val stringResId: Int) {
    All(-1, R.string.all),
    Action(28, R.string.action),
    Adventure(12, R.string.adventure),
    Animation(16, R.string.animation),
    Comedy(35, R.string.comedy),
    Crime(80, R.string.crime),
    Documentary(99, R.string.documentary),
    Drama(18, R.string.drama),
    Family(10751, R.string.family),
    Fantasy(14, R.string.fantasy),
    History(36, R.string.history),
    Horror(27, R.string.horror),
    Music(10402, R.string.music),
    Mystery(9648, R.string.mystery),
    Romance(10749, R.string.romance),
    SciFi(878, R.string.sci_fi),
    TvMovie(10770, R.string.tv_movie),
    Thriller(53, R.string.thriller),
    War(10752, R.string.war),
    Western(37, R.string.western),
}
