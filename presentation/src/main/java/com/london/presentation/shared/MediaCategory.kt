package com.london.presentation.shared

import com.london.designsystem.component.Tabbable
import com.london.presentation.R

enum class MediaCategory : Tabbable {
    MOVIES {
        override val tabTextResId: Int
            get() = R.string.Movies
    },
    TV_SHOWS {
        override val tabTextResId: Int
            get() = R.string.TV_Shows
    }
}
