package com.london.domain.repository

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

interface TvShowRepository {
    suspend fun getTvShowDetailsById(id: Int): TvShowDetailsEntity
    suspend fun getImagesTvShowById(id: Int): TvShowImagesEntity
    suspend fun getActorTvShowPicksById(id: Int): CastDetails
    suspend fun getPopularTvShows(): List<PopularMedia>
    suspend fun addTvShowById(id: Int, rating: Int): Boolean
    suspend fun getAllRatedTvShows(): List<RatedMedia>
    suspend fun deleteTvShowRating(id: Int): Boolean
    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending>

    suspend fun getTopRatedTvShows(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia>

    suspend fun getTvShowsByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<TvShow>

    suspend fun addTvShowEpisode(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean

    suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int,
    ): TvShowEpisodesEntity

    suspend fun getTvShowEpisodeByPosition(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity

    suspend fun getEpisodeVideos(seriesId: Int, seasonNumber: Int, episodeNumber: Int): List<String>
    suspend fun getTvShowVideos(id: Int): List<String>
    suspend fun getTvShowReviews(id: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
    suspend fun getAccountTvShowStateById(
        id: Int,
    ): MediaStates

    suspend fun getAccountTvEpisode(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaStates
}