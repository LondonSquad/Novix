package com.london.domain.repository

import com.london.domain.entity.actor.ActorMediaDetails
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.Review
import com.london.domain.entity.shared.ImagesEntity
import com.london.domain.entity.shared.MediaStates
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.entity.shared.RatedMedia
import com.london.domain.entity.shared.Trending
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.entity.tvshow.TvShowDetails
import com.london.domain.entity.tvshow.episode.EpisodeDetails
import com.london.domain.entity.tvshow.episode.SeasonEpisodes

interface TvShowRepository {

    suspend fun getTvShowDetailsById(id: Int): TvShowDetails

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

    suspend fun getTvShowSeasonEpisodes(
        tvShowId: Int,
        seasonNumber: Int,
    ): SeasonEpisodes

    suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeDetails

    suspend fun getEpisodeVideos(tvShowId: Int, seasonNumber: Int, episodeNumber: Int): List<String>

    suspend fun getTvSeasonTrailer(tvShowId: Int, seasonNumber: Int): List<String>

    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): PagedFetchResponse<Review>

    suspend fun getAccountTvShowStateById(
        id: Int,
    ): MediaStates

    suspend fun getAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaStates

}
