@file:KoverIgnore
package com.london.domain.entity.popular

import com.london.domain.KoverIgnore

data class PopularMovie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
)