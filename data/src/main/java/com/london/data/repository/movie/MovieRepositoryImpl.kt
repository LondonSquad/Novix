package com.london.data.repository.movie

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.genre.getId
import com.london.data.mapper.home.popular.toMovieEntity
import com.london.data.mapper.home.popular.toPopularMovieSectionLocal
import com.london.data.mapper.home.popular.toPopularMovies
import com.london.data.mapper.home.toprated.toEntity
import com.london.data.mapper.home.toprated.toLocal
import com.london.data.mapper.home.trending.toEntityMedia
import com.london.data.mapper.myrating.toEntity
import com.london.data.mapper.search.toEntity
import com.london.data.mapper.search.toLocal
import com.london.data.mapper.search.toReviewEntity
import com.london.data.remote.source.movie.MovieRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.MovieImages
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val crashReporter: CrashReporter,
    private val authenticationPreferences: AuthenticationPreferences,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val upComingLocalDataSource: UpComingLocalDataSource,
    private val localTopRated: HomeLocalDataSource<TopRatedLocal>,
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>
) : MovieRepository {

    override suspend fun getMovieById(id: Int): MovieDetails =
        movieRemoteDataSource.getMovieDetails(id).getOrThrow().toEntity()

    override suspend fun getMovieImagesById(id: Int): MovieImages =
        movieRemoteDataSource.getMovieImages(id).getOrThrow().toEntity()

    override suspend fun getActorMoviePicksById(id: Int): ActorMediaDetails =
        movieRemoteDataSource.getActorMovieById(id).getOrThrow().toEntity()

    override suspend fun getSimilarMoviesById(id: Int): List<Movie> {
        return movieRemoteDataSource.getSimilarMovies(id).getOrThrow().items
            .map { it.toEntity() }
    }

    override suspend fun getMovieVideos(movieId: Int): List<String> {
        return movieRemoteDataSource.getMovieVideos(movieId)
            .getOrThrow().movies.orEmpty().map { movieVideoRemote ->
                movieVideoRemote.key.asImageUrlOrEmpty()
            }
    }

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            movieRemoteDataSource.getMovieReviews(
                movieId, pageNumber
            ).getOrThrow().toReviewEntity()
        }).run {
        PagedFetchResponse(
            currentPage = currentPage,
            items = items,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }

    override suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> {
        val response = movieRemoteDataSource.getTrendingMovies(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityMedia(MediaType.Movie) },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getUpcomingMoviesByGenre(
        genre: MovieGenre, pageNumber: Int
    ): PagedFetchResponse<UpComingMovie> = fetchAndSync(
        cacheBlock = {
            upComingLocalDataSource.getUpComingMoviesPage(
                page = pageNumber,
                categoryId = genre.getId()
            )
        },
        crashReporter = crashReporter,
        syncBlock = { upComingLocalDataSource.insert(it) },
        networkBlock = {
            movieRemoteDataSource.getUpComingMoviesByCategory(
                categoryId = genre.getId(),
                pageNumber = pageNumber,
            ).getOrThrow().toLocal(genre.getId())
        }).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toEntity() },
            totalPages = totalPages,
            totalItems = totalResults
        )
    }

    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia> = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            movieRemoteDataSource
                .getTopRatedMovies(pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedMovies ->
            localTopRated.insertAll(topRatedMovies.map { it.toLocal() })
        },
        crashReporter = crashReporter
    ).run {
        val remoteResult = movieRemoteDataSource.getTopRatedMovies(pageNumber).getOrThrow()
        val movies = remoteResult.items.map { it.toEntity() }
        PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            currentPage = remoteResult.currentPage,
            items = movies,
            totalItems = remoteResult.totalItems
        )
    }

    override suspend fun getFirstPageTopRatedMovies() = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            movieRemoteDataSource
                .getTopRatedMovies(pageNumber = PAGE_NUMBER)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedMovies ->
            localTopRated.insertAll(topRatedMovies.map { it.toLocal() })
        },
        crashReporter = crashReporter
    )

    override suspend fun getMoviesByGenre(
        genre: MovieGenre,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
        val response =
            movieRemoteDataSource.getMoviesByCategory(genre.getId(), pageNumber).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getAllRatedMovies(): List<RatedMedia> =
        movieRemoteDataSource.getAllRatedMovies(
            accountId = authenticationPreferences.getAccountId(),
            sessionId = authenticationPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.Movie) }

    override suspend fun deleteMovieRating(movieId: Int): Boolean =
        movieRemoteDataSource.deleteMovieRating(
            movieId = movieId,
            sessionId = authenticationPreferences.getSessionId()
        ).isSuccess

    override suspend fun getPopularMovies(): List<PopularMedia> = fetchAndSync(
        cacheBlock = {
            val local = homeLocalDataSource.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toMovieEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            movieRemoteDataSource.getPopularMovies().getOrThrow().toPopularMovies()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularMovieSectionLocal(MediaType.Movie) })
        },
        crashReporter = crashReporter
    )

    override suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean = movieRemoteDataSource.addMovieRating(
        movieId = id,
        rating = rating.toDouble(),
        userSessionId = authenticationPreferences.getSessionId(),
        guestSessionId = authenticationPreferences.getGuestSessionId()
    ).isSuccess

    override suspend fun getAccountMovieStatesById(
        id: Int,
    ): MediaStates {
        return movieRemoteDataSource.getAccountMovieStates(
            movieId = id,
            userSessionId = authenticationPreferences.getSessionId(),
        ).getOrThrow().toEntity()
    }

    companion object {
        const val PAGE_NUMBER = 1
    }
}
