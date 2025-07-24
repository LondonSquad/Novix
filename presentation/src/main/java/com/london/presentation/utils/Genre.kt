package com.london.presentation.utils

import androidx.annotation.StringRes
import com.london.presentation.R

enum class MovieGenre(
    val id: Int,
    @StringRes val stringResId: Int
) {
    All(id = -1, stringResId = R.string.all),
    Action(id = 28, stringResId = R.string.action),
    Adventure(id = 12, stringResId = R.string.adventure),
    Animation(id = 16, stringResId = R.string.animation),
    Comedy(id = 35, stringResId = R.string.comedy),
    Crime(id = 80, stringResId = R.string.crime),
    Documentary(id = 99, stringResId = R.string.documentary),
    Drama(id = 18, stringResId = R.string.drama),
    Family(id = 10751, stringResId = R.string.family),
    Fantasy(id = 14, stringResId = R.string.fantasy),
    History(id = 36, stringResId = R.string.history),
    Horror(id = 27, stringResId = R.string.horror),
    Music(id = 10402, stringResId = R.string.music),
    Mystery(id = 9648, stringResId = R.string.mystery),
    Romance(id = 10749, stringResId = R.string.romance),
    SciFi(id = 878, stringResId = R.string.sci_fi),
    TvMovie(id = 10770, stringResId = R.string.tv_movie),
    Thriller(id = 53, stringResId = R.string.thriller),
    War(id = 10752, stringResId = R.string.war),
    Western(id = 37, stringResId = R.string.western),
}

enum class TvShowGenre(
    val id: Int,
    @StringRes val stringResId: Int
) {
    All(id = -1, stringResId = R.string.all),
    ActionAdventure(id = 10759, stringResId = R.string.action_adventure),
    Animation(id = 16, stringResId = R.string.animation),
    Comedy(id = 35, stringResId = R.string.comedy),
    Crime(id = 80, stringResId = R.string.crime),
    Documentary(id = 99, stringResId = R.string.documentary),
    Drama(id = 18, stringResId = R.string.drama),
    Family(id = 10751, stringResId = R.string.family),
    Kids(id = 10762, stringResId = R.string.kids),
    Mystery(id = 9648, stringResId = R.string.mystery),
    News(id = 10763, stringResId = R.string.news),
    Reality(id = 10764, stringResId = R.string.reality),
    Fantasy(id = 10765, stringResId = R.string.fantasy),
    Soap(id = 10766, stringResId = R.string.soap),
    Talk(id = 10767, stringResId = R.string.talk),
    WarPolitics(id = 10768, stringResId = R.string.war_politics),
    Western(id = 37, stringResId = R.string.western),
}


