package com.london.presentation.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.london.presentation.R

enum class MovieGenre(
    val id: Int,
    @StringRes val stringResId: Int,
    @DrawableRes val backgroundResId: Int? = null
) {
    All(id = -1, stringResId = R.string.all),
    Action(
        id = 28,
        stringResId = R.string.action,
        backgroundResId = R.drawable.img_action_background
    ),
    Adventure(
        id = 12,
        stringResId = R.string.adventure,
        backgroundResId = R.drawable.img_adventure_background
    ),
    Animation(
        id = 16,
        stringResId = R.string.animation,
        backgroundResId = R.drawable.img_animation_background
    ),
    Comedy(
        id = 35,
        stringResId = R.string.comedy,
        backgroundResId = R.drawable.img_comedy_background
    ),
    Crime(id = 80, stringResId = R.string.crime, backgroundResId = R.drawable.img_crime_background),
    Documentary(
        id = 99,
        stringResId = R.string.documentary,
        backgroundResId = R.drawable.img_documentary_background
    ),
    Drama(id = 18, stringResId = R.string.drama, backgroundResId = R.drawable.img_drama_background),
    Family(
        id = 10751,
        stringResId = R.string.family,
        backgroundResId = R.drawable.img_family_background
    ),
    Fantasy(
        id = 14,
        stringResId = R.string.fantasy,
        backgroundResId = R.drawable.img_fantasy_background
    ),
    History(
        id = 36,
        stringResId = R.string.history,
        backgroundResId = R.drawable.img_history_background
    ),
    Horror(
        id = 27,
        stringResId = R.string.horror,
        backgroundResId = R.drawable.img_horror_background
    ),
    Music(
        id = 10402,
        stringResId = R.string.music,
        backgroundResId = R.drawable.img_music_background
    ),
    Mystery(
        id = 9648,
        stringResId = R.string.mystery,
        backgroundResId = R.drawable.img_mystery_background
    ),
    Romance(
        id = 10749,
        stringResId = R.string.romance,
        backgroundResId = R.drawable.img_romance_background
    ),
    SciFi(
        id = 878,
        stringResId = R.string.sci_fi,
        backgroundResId = R.drawable.img_sci_fi_and_fantasy_background
    ),
    TvMovie(
        id = 10770,
        stringResId = R.string.tv_movie,
        backgroundResId = R.drawable.img_tv_movie_background
    ),
    Thriller(
        id = 53,
        stringResId = R.string.thriller,
        backgroundResId = R.drawable.img_thriller_background
    ),
    War(id = 10752, stringResId = R.string.war, backgroundResId = R.drawable.img_war_background),
    Western(
        id = 37,
        stringResId = R.string.western,
        backgroundResId = R.drawable.img_western_background
    ),
}

enum class TvShowGenre(
    val id: Int,
    @StringRes val stringResId: Int,
    @DrawableRes val backgroundResId: Int? = null
) {
    All(id = -1, stringResId = R.string.all),
    ActionAdventure(
        id = 10759,
        stringResId = R.string.action_adventure,
        backgroundResId = R.drawable.img_action_and_adventure_background
    ),
    Animation(
        id = 16,
        stringResId = R.string.animation,
        backgroundResId = R.drawable.img_animation_background
    ),
    Comedy(
        id = 35,
        stringResId = R.string.comedy,
        backgroundResId = R.drawable.img_comedy_background
    ),
    Crime(id = 80, stringResId = R.string.crime, backgroundResId = R.drawable.img_crime_background),
    Documentary(
        id = 99,
        stringResId = R.string.documentary,
        backgroundResId = R.drawable.img_documentary_background
    ),
    Drama(id = 18, stringResId = R.string.drama, backgroundResId = R.drawable.img_drama_background),
    Family(
        id = 10751,
        stringResId = R.string.family,
        backgroundResId = R.drawable.img_family_background
    ),
    Kids(id = 10762, stringResId = R.string.kids, backgroundResId = R.drawable.img_kids_background),
    Mystery(
        id = 9648,
        stringResId = R.string.mystery,
        backgroundResId = R.drawable.img_mystery_background
    ),
    News(id = 10763, stringResId = R.string.news, backgroundResId = R.drawable.img_news_background),
    Reality(
        id = 10764,
        stringResId = R.string.reality,
        backgroundResId = R.drawable.img_reality_background
    ),
    Fantasy(
        id = 10765,
        stringResId = R.string.fantasy,
        backgroundResId = R.drawable.img_fantasy_background
    ),
    Soap(id = 10766, stringResId = R.string.soap, backgroundResId = R.drawable.img_soap_background),
    Talk(id = 10767, stringResId = R.string.talk, backgroundResId = R.drawable.img_talk_background),
    WarPolitics(
        id = 10768,
        stringResId = R.string.war_politics,
        backgroundResId = R.drawable.img_war_background
    ),
    Western(
        id = 37,
        stringResId = R.string.western,
        backgroundResId = R.drawable.img_western_background
    ),
}

