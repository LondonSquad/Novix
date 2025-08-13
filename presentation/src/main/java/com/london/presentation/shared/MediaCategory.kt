package com.london.presentation.shared

import com.london.designsystem.component.Tabbable
import com.london.presentation.R

enum class MediaCategory : Tabbable {
    Movies {
        override val tabTextResId: Int
            get() = R.string.Movies
    },
    TvShows {
        override val tabTextResId: Int
            get() = R.string.TV_Shows
    }
}
