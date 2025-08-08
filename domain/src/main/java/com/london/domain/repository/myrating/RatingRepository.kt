package com.london.domain.repository.myrating

import com.london.domain.entity.RatedMedia


interface RatingRepository {
    suspend fun getAllRatedMedia(): List<RatedMedia>
}