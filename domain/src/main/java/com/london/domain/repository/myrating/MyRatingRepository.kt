package com.london.domain.repository.myrating

import com.london.domain.entity.myrating.RatedMedia

interface MyRatingRepository {
    suspend fun getAllRatedMedia(): List<RatedMedia>
}