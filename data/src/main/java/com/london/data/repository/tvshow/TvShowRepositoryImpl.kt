package com.london.data.repository.tvshow

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.details.tvshow.TvShowImagesMapper.toEntity
import com.london.data.mapper.details.tvshow.toEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodeEntity
import com.london.data.mapper.details.tvshow.toTvShowEpisodesEntity
import com.london.data.mapper.home.popular.toPopularTvShowSectionLocal
import com.london.data.mapper.home.popular.toPopularTvShows
import com.london.data.mapper.home.popular.toTvShowEntity
import com.london.data.mapper.home.toprated.toEntity
import com.london.data.mapper.home.toprated.toLocal
import com.london.data.mapper.home.trending.toEntityMedia
import com.london.data.mapper.myrating.toEntity
import com.london.data.mapper.search.toEntity
import com.london.data.mapper.search.toReviewEntity
import com.london.data.remote.source.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    private val authPreferences: AuthPreferences,
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
    private val localTopRated: HomeLocalDataSource<TopRatedLocal>,
    private val crashReporter: CrashReporter
) : TvShowRepository {

    override suspend fun getTvShowDetailsById(
        id: Int,
    ): TvShowDetailsEntity = tvShowDetailsRemoteDataSource.getTvShowDetailsById(
        id = id,
    ).getOrThrow().toEntity()

    override suspend fun getImagesTvShowById(id: Int): TvShowImagesEntity =
        tvShowDetailsRemoteDataSource.getTvShowImagesById(id).getOrThrow().toEntity()

    override suspend fun getActorTvShowPicksById(id: Int): CastDetails =
        tvShowDetailsRemoteDataSource.getActorTvShowById(id).getOrThrow().toEntity()

    override suspend fun getPopularTvShows(): List<PopularMedia> = fetchAndSync(
        cacheBlock = {
            val local = homeLocalDataSource.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toTvShowEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            tvShowDetailsRemoteDataSource.getPopularTvShows().getOrThrow().toPopularTvShows()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularTvShowSectionLocal(MediaType.TvShow) })
        },
        crashReporter = crashReporter
    )

    override suspend fun addTvShowById(id: Int, rating: Int): Boolean =
        tvShowDetailsRemoteDataSource.addTvShowRating(
            tvShowId = id,
            rating = rating.toDouble(),
            userSessionId = authPreferences.getSessionId(),
            guestSessionId = authPreferences.getGuestSessionId()
        ).isSuccess

    override suspend fun getAllRatedTvShows(): List<RatedMedia> =
        tvShowDetailsRemoteDataSource.getAllRatedTvShows(
            accountId = authPreferences.getAccountId(),
            sessionId = authPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.TvShow) }

    override suspend fun deleteTvShowRating(tvShowId: Int): Boolean =
        tvShowDetailsRemoteDataSource.deleteTvShowRating(
            tvShowId = tvShowId,
            sessionId = authPreferences.getSessionId()
        ).isSuccess

    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> {
        val response = tvShowDetailsRemoteDataSource.getTrendingTvShows(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityMedia() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTopRatedTvShows(
        pageNumber: Int
    ): PagedFetchResponse<TopRatedMedia> = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            tvShowDetailsRemoteDataSource
                .getTopRatedTvShows(pageNumber = pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedTvShows ->
            localTopRated.insertAll(topRatedTvShows.map { it.toLocal() })
        },
        crashReporter = crashReporter
    ).run {
        val remoteResult =
            tvShowDetailsRemoteDataSource.getTopRatedTvShows(pageNumber = pageNumber)
                .getOrThrow()
        PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            items = remoteResult.items.map { it.toEntity() },
            currentPage = remoteResult.currentPage,
            totalItems = remoteResult.totalItems,
        )
    }

    override suspend fun getTvShowsByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<TvShow> {
        val response = tvShowDetailsRemoteDataSource.getTvShowsByCategoryId(categoryId, pageNumber)
            .getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean = tvShowDetailsRemoteDataSource.addTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId(),
        rating = rating.toDouble()
    ).isSuccess

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int, seasonNumber: Int
    ): TvShowEpisodesEntity =

        tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(
            id = tvShowId, seasonNumber = seasonNumber
        ).getOrThrow().toTvShowEpisodesEntity()


    override suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): TvShowEpisodeByIdEntity =
        tvShowDetailsRemoteDataSource.getEpisodeDetailsByPosition(
            tvShowId = tvShowId, seasonNumber = seasonNumber, episodeNumber = episodeNumber
        ).getOrThrow().toTvShowEpisodeEntity()

    override suspend fun getEpisodeVideos(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowDetailsRemoteDataSource.getEpisodeVideos(
            tvShowId = seriesId, seasonNumber = seasonNumber, episodeNumber = episodeNumber
        ).getOrThrow().results?.map { it.key.asYoutubeUrlOrEmpty() }.orEmpty()

    override suspend fun getTvShowVideos(tvShowId: Int): List<String> =
        tvShowDetailsRemoteDataSource.getTvShowVideos(tvShowId)
            .getOrThrow().tvShow?.map { it.key.asYoutubeUrlOrEmpty() }.orEmpty()

    override suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            tvShowDetailsRemoteDataSource.getTvShowReviews(tvShowId, pageNumber).getOrThrow()
                .toReviewEntity()
        }).run {
        PagedFetchResponse(
            currentPage = currentPage,
            items = items,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }

    override suspend fun getAccountTvShowState(
        tvShowId: Int,
    ): MediaStates = tvShowDetailsRemoteDataSource.getAccountTvShowStates(
        tvShowId = tvShowId,
        guestSessionId = authPreferences.getGuestSessionId(),
        userSessionId = authPreferences.getSessionId()
    ).getOrThrow().toEntity()

    override suspend fun getAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaStates = tvShowDetailsRemoteDataSource.getAccountTvEpisodeState(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        guestSessionId = authPreferences.getGuestSessionId(),
        userSessionId = authPreferences.getSessionId(),
    ).getOrThrow().toEntity()
}