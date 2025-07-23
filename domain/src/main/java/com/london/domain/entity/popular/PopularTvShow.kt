@file:KoverIgnore
package com.london.domain.entity.popular

import com.london.domain.KoverIgnore

data class PopularTvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val posterUrl: String,
    val rating: Double
)
