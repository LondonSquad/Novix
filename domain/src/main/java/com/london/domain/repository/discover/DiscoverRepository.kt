package com.london.domain.repository.discover

import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow

interface DiscoverRepository {

    suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<Movie>

    suspend fun getTvShowsByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<TvShow>
}
