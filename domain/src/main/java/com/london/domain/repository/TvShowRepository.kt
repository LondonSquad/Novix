package com.london.domain.repository

import com.london.domain.entity.ImagesEntity
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

interface TvShowRepository {
    suspend fun getTvShowDetailsById(id: Int): TvShowDetailsEntity
    suspend fun getImagesTvShowById(id: Int): ImagesEntity
    suspend fun getActorTvShowPicksById(id: Int): ActorMediaDetails
    suspend fun getPopularTvShows(): List<PopularMedia>
    suspend fun addTvShowById(id: Int, rating: Int): Boolean
    suspend fun getAllRatedTvShows(): List<RatedMedia>
    suspend fun deleteTvShowRating(tvShowId: Int): Boolean
    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending>
    suspend fun getFirstPageTopRatedTvShows(): List<TopRatedMedia>

    suspend fun getTopRatedTvShows(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia>

    suspend fun getTvShowsByGenre(
        genre: TvShowGenre,
        pageNumber: Int
    ): PagedFetchResponse<TvShow>

    suspend fun addTvShowEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean

    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesEntity

    suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity

    suspend fun getEpisodeVideos(seriesId: Int, seasonNumber: Int, episodeNumber: Int): List<String>
    suspend fun getTvShowVideos(tvShowId: Int): List<String>
    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
    suspend fun getAccountTvShowState(
        tvShowId: Int,
    ): MediaStates

    suspend fun getAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaStates
}