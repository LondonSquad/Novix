package com.london.data.repository.tvshow

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthenticationPreferences
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
import com.london.data.remote.source.tvshow.TvShowRemoteDataSource
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
    private val tvShowRemoteDataSource: TvShowRemoteDataSource,
    private val authenticationPreferences: AuthenticationPreferences,
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
    private val localTopRated: HomeLocalDataSource<TopRatedLocal>,
    private val crashReporter: CrashReporter
) : TvShowRepository {

    override suspend fun getTvShowDetailsById(id: Int): TvShowDetailsEntity {
        return tvShowRemoteDataSource.getTvShowDetailsById(
            id = id,
        ).getOrThrow().toEntity()
    }

    override suspend fun getImagesTvShowById(id: Int): TvShowImagesEntity =
        tvShowRemoteDataSource.getTvShowImagesById(id).getOrThrow().toEntity()

    override suspend fun getActorTvShowById(id: Int): CastDetails =
        tvShowRemoteDataSource.getActorTvShowById(id).getOrThrow().toEntity()

    override suspend fun getPopularTvShows(): List<PopularMedia> = fetchAndSync(
        cacheBlock = {
            val local = homeLocalDataSource.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toTvShowEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            tvShowRemoteDataSource.getPopularTvShows().getOrThrow().toPopularTvShows()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularTvShowSectionLocal(MediaType.TvShow) })
        },
        crashReporter = crashReporter
    )

    override suspend fun addTvShowById(id: Int, rating: Int): Boolean =
        tvShowRemoteDataSource.addTvShowRating(
            tvShowId = id,
            rating = rating.toDouble(),
            userSessionId = authenticationPreferences.getSessionId(),
            guestSessionId = authenticationPreferences.getGuestSessionId()
        ).isSuccess

    override suspend fun getAllRatedTvShows(): List<RatedMedia> =
        tvShowRemoteDataSource.getAllRatedTvShows(
            accountId = authenticationPreferences.getAccountId(),
            sessionId = authenticationPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.TvShow) }

    override suspend fun deleteTvShowRating(tvShowId: Int): Boolean =
        tvShowRemoteDataSource.deleteTvShowRating(
            tvShowId = tvShowId,
            sessionId = authenticationPreferences.getSessionId()
        ).isSuccess

    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> {
        val response = tvShowRemoteDataSource.getTrendingTvShows(page).getOrThrow()
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
            tvShowRemoteDataSource
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
            tvShowRemoteDataSource.getTopRatedTvShows(pageNumber = pageNumber)
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
        val response = tvShowRemoteDataSource.getTvShowsByCategoryId(categoryId, pageNumber)
            .getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun addTvShowEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean = tvShowRemoteDataSource.addTvShowEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        userSessionId = authenticationPreferences.getSessionId(),
        guestSessionId = authenticationPreferences.getGuestSessionId(),
        rating = rating.toDouble()
    ).isSuccess

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int, seasonNumber: Int
    ): TvShowEpisodesEntity =

        tvShowRemoteDataSource.getTvShowEpisodesBySeason(
            id = tvShowId, seasonNumber = seasonNumber
        ).getOrThrow().toTvShowEpisodesEntity()


    override suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity =
        tvShowRemoteDataSource.getEpisodeDetails(
            tvShowId = tvShowId, seasonNumber = seasonNumber, episodeNumber = episodeNumber
        ).getOrThrow().toTvShowEpisodeEntity()

    override suspend fun getEpisodeVideos(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowRemoteDataSource.getEpisodeVideos(
            tvShowId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        ).getOrThrow().results?.map { it.key.asYoutubeUrlOrEmpty() }.orEmpty()

    override suspend fun getTvShowVideos(tvShowId: Int): List<String> =
        tvShowRemoteDataSource.getTvShowVideos(tvShowId)
            .getOrThrow().tvShow?.map { it.key.asYoutubeUrlOrEmpty() }.orEmpty()

    override suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            tvShowRemoteDataSource.getTvShowReviews(tvShowId, pageNumber).getOrThrow()
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
    ): MediaStates = tvShowRemoteDataSource.getAccountTvShowStates(
        tvShowId = tvShowId,
        guestSessionId = authenticationPreferences.getGuestSessionId(),
        userSessionId = authenticationPreferences.getSessionId()
    ).getOrThrow().toEntity()

    override suspend fun getAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaStates = tvShowRemoteDataSource.getAccountTvEpisodeState(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        guestSessionId = authenticationPreferences.getGuestSessionId(),
        userSessionId = authenticationPreferences.getSessionId(),
    ).getOrThrow().toEntity()
}