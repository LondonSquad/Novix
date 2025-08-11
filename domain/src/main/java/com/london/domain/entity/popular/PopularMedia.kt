@file:KoverIgnore
package com.london.domain.entity.popular

import com.london.domain.KoverIgnore
import com.london.domain.entity.recent.MediaType

data class PopularMedia(
    val id: Int,
    val name: String,
    val posterUrl: String,
    val rating: Double,
    val mediaType: MediaType
)