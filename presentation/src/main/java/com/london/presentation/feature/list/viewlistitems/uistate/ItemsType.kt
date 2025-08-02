package com.london.presentation.feature.list.viewlistitems.uistate

import androidx.annotation.StringRes
import com.london.presentation.R

enum class ItemsType(@StringRes val titleId: Int) {
    All(titleId = R.string.all),
    Movies(titleId = R.string.movies),
    TvShows(titleId = R.string.tv_shows),
}