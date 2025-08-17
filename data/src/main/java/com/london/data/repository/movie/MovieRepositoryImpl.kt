package com.london.data.repository.movie

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.model.home.upcoming.UpComingSectionLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.details.toEntity
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
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.ImagesEntity
import com.london.domain.entity.MediaStates
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.moviedatails.MovieDetails
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

    override suspend fun getMovieImagesById(id: Int): ImagesEntity =
        movieRemoteDataSource.getMovieImages(id).getOrThrow().toEntity()

    override suspend fun getActorMoviePicksById(id: Int): ActorMediaDetails =
        movieRemoteDataSource.getActorMovieById(id).getOrThrow().toEntity()

    override suspend fun getSimilarMoviesById(id: Int) =
        movieRemoteDataSource.getSimilarMovies(id).getOrThrow().items.map { it.toEntity() }


    override suspend fun getMovieVideos(movieId: Int): List<String> {
        return movieRemoteDataSource.getMovieVideos(movieId)
            .getOrThrow().videos.orEmpty().map { movieVideoRemote ->
                movieVideoRemote.youtubeKey.asYoutubeUrlOrEmpty()
            }
    }

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            getMovieReviewsFromRemote(movieId = movieId, pageNumber = pageNumber)
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
        genre: MovieGenre,
        pageNumber: Int
    ): PagedFetchResponse<UpComingMovie> = fetchAndSync(
        cacheBlock = { getUpComingCachedMovies(pageNumber = pageNumber, genre = genre) },
        crashReporter = crashReporter,
        syncBlock = { upComingLocalDataSource.insert(it) },
        networkBlock = { getUpcomingMovies(pageNumber = pageNumber, genre = genre) })
        .run {
            PagedFetchResponse(
                currentPage = page,
                items = results.map { it.toEntity() },
                totalPages = totalPages,
                totalItems = totalResults
            )
        }

    override suspend fun getTopRatedMovies(pageNumber: Int): PagedFetchResponse<TopRatedMedia> =
        fetchAndSync(
            cacheBlock = { getTopRatedCashedMovies() },
            networkBlock = { getTopRatedRemoteMovies(pageNumber) },
            syncBlock = { topRatedMovies ->
                localTopRated.insertAll(topRatedMovies.map { it.toLocal() })
            },
            crashReporter = crashReporter
        ).run { getTopRatedPages(pageNumber) }

    override suspend fun getFirstPageTopRatedMovies() = fetchAndSync(
        cacheBlock = { getTopRatedCashedMovies() },
        networkBlock = { fetchFirstTopRatedPageMovies() },
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

    override suspend fun getAllRatedMovies(): List<RatedMedia> {
        return movieRemoteDataSource.getAllRatedMovies(
            accountId = authenticationPreferences.getAccountId(),
            sessionId = authenticationPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.Movie) }
    }

    override suspend fun deleteMovieRating(movieId: Int): Boolean {
        return movieRemoteDataSource.deleteMovieRating(
            movieId = movieId,
            sessionId = authenticationPreferences.getSessionId()
        ).isSuccess
    }

    override suspend fun getPopularMovies(): List<PopularMedia> = fetchAndSync(
        cacheBlock = { getCachedPopularMovies() },
        networkBlock = {
            movieRemoteDataSource.getPopularMovies().getOrThrow().toPopularMovies()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularMovieSectionLocal(MediaType.Movie) })
        },
        crashReporter = crashReporter
    )

    override suspend fun addMovieRatingById(id: Int, rating: Int): Boolean {
        return movieRemoteDataSource.addMovieRating(
            movieId = id,
            rating = rating.toDouble(),
            userSessionId = authenticationPreferences.getSessionId(),
            guestSessionId = authenticationPreferences.getGuestSessionId()
        ).isSuccess
    }

    override suspend fun getAccountMovieStatesById(id: Int): MediaStates {
        return movieRemoteDataSource.getAccountMovieStates(
            movieId = id,
            userSessionId = authenticationPreferences.getSessionId(),
        ).getOrThrow().toEntity()
    }

    private suspend fun getMovieReviewsFromRemote(
        movieId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> {
        return movieRemoteDataSource.getMovieReviews(
            movieId, pageNumber
        ).getOrThrow().toReviewEntity()
    }

    private suspend fun getUpcomingMovies(
        pageNumber: Int, genre: MovieGenre
    ): UpComingSectionLocal {
        return movieRemoteDataSource.getUpComingMoviesByCategory(
            categoryId = if (genre == MovieGenre.ALL) null else genre.getId(),
            pageNumber = pageNumber,
        ).getOrThrow().toLocal(genre.getId())
    }

    private suspend fun getUpComingCachedMovies(
        pageNumber: Int, genre: MovieGenre
    ): UpComingSectionLocal {
        return upComingLocalDataSource.getUpComingMoviesPage(
            page = pageNumber,
            categoryId = if (genre == MovieGenre.ALL) null else genre.getId()
        )
    }

    private suspend fun getTopRatedPages(pageNumber: Int): PagedFetchResponse<TopRatedMedia> {
        val remoteResult = movieRemoteDataSource.getTopRatedMovies(pageNumber).getOrThrow()
        val movies = remoteResult.items.map { it.toEntity() }
        return PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            currentPage = remoteResult.currentPage,
            items = movies,
            totalItems = remoteResult.totalItems
        )
    }

    private suspend fun getTopRatedRemoteMovies(pageNumber: Int): List<TopRatedMedia> {
        return movieRemoteDataSource
            .getTopRatedMovies(pageNumber)
            .getOrThrow()
            .items.map { it.toEntity() }
    }

    private suspend fun fetchFirstTopRatedPageMovies(): List<TopRatedMedia> {
        return movieRemoteDataSource
            .getTopRatedMovies(pageNumber = PAGE_NUMBER)
            .getOrThrow()
            .items.map { it.toEntity() }
    }

    private suspend fun getTopRatedCashedMovies(): List<TopRatedMedia>? {
        val local = localTopRated.getAll()
            .filter { it.mediaType == MediaType.Movie }
            .map { it.toEntity() }
        return local.takeIf { it.isNotEmpty() }
    }

    private suspend fun getCachedPopularMovies(): List<PopularMedia>? {
        val local = homeLocalDataSource.getAll()
            .filter { it.mediaType == MediaType.Movie }
            .map { it.toMovieEntity() }
        return local.takeIf { it.isNotEmpty() }
    }

    companion object {
        const val PAGE_NUMBER = 1
    }
}
