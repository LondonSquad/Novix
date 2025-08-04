package com.london.data.di

import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.local.source.recent.RecentDataSource
import com.london.data.local.source.recent.watched.RecentWatchedDataSource
import com.london.data.remote.service.home.TrendingApiService
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSource
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSource
import com.london.data.remote.source.details.movie.rating.MovieRatingRemoteDataSource
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.details.videoprovider.movie.MovieVideoProviderRemote
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.remote.source.discover.DiscoverRemoteDataSource
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.data.remote.source.home.trending.TrendingRemoteDataSourceImpl
import com.london.data.remote.source.home.upcoming.UpComingRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import com.london.data.repository.discover.DiscoverRepositoryImpl
import com.london.data.repository.home.popular.PopularRepositoryImpl
import com.london.data.repository.home.toprated.TopRatedMovieRepositoryImpl
import com.london.data.repository.home.toprated.TopRatedTvSeriesRepositoryImpl
import com.london.data.repository.home.trending.TrendingRepositoryImpl
import com.london.data.repository.home.upcoming.UpComingRepositoryImpl
import com.london.data.repository.popular.PopularRepositoryImpl
import com.london.data.repository.rating.RatingRepositoryImpl
import com.london.data.repository.recent.RecentSearchRepositoryImpl
import com.london.data.repository.recent.RecentViewedRepositoryImpl
import com.london.data.repository.recent.RecentWatchedRepositoryIml
import com.london.data.repository.search.ActorRepositoryImpl
import com.london.data.repository.search.MovieDetailsRepositoryImpl
import com.london.data.repository.search.MovieVideoProviderRepositoryImpl
import com.london.data.repository.search.SearchRepositoryImpl
import com.london.data.repository.search.TvShowRepositoryImpl
import com.london.data.repository.search.TvShowVideoProviderRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.FirebaseCrashReporter
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.AuthRepository
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.MovieVideoProviderRepository
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.RatingRepository
import com.london.domain.repository.RecentRepository
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.repository.TvShowVideoProviderRepository
import com.london.domain.repository.UpComingRepository
import com.london.domain.repository.discover.DiscoverRepository
import com.london.domain.repository.toprated.TopRatedMovieRepository
import com.london.domain.repository.toprated.TopRatedTvSeriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        authRemoteDataSource: AuthenticationRemoteDataSource,
        authPreferences: AuthPreferences
    ): AuthRepository = AuthenticationRepositoryImpl(authRemoteDataSource, authPreferences)

    @Provides
    @Singleton
    fun providePopularRepository(
        dataSource: PopularRemoteDataSource,
        @Named("popularLocalDataSource") homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
        crashReporter: CrashReporter
    ): PopularRepository = PopularRepositoryImpl(
        popularRemoteDataSource = dataSource,
        homeLocalDataSource = homeLocalDataSource,
        crashReporter = crashReporter
    )

    @Provides
    @Singleton
    fun provideRecentSearchRepository(
        dataSource: RecentDataSource<RecentSearchLocal>
    ): RecentRepository<RecentSearch> =
        RecentSearchRepositoryImpl(recentSearchLocalDataSource = dataSource)

    @Provides
    @Singleton
    fun provideRecentViewedRepository(
        dataSource: RecentDataSource<RecentViewedLocal>
    ): RecentRepository<RecentViewed> =
        RecentViewedRepositoryImpl(recentRecentViewedLocalDataSource = dataSource)

    @Provides
    @Singleton
    fun provideRecentWatchedRepository(
        recentWatchedMoviesDataSource: RecentWatchedDataSource<RecentWatchedMovieLocal>,
        recentWatchedTvShowsDataSource: RecentWatchedDataSource<RecentWatchedTvShowLocal>
    ): RecentWatchedRepository = RecentWatchedRepositoryIml(
        recentWatchedMoviesDataSource = recentWatchedMoviesDataSource,
        recentWatchedTvShowsDataSource = recentWatchedTvShowsDataSource
    )

    @Provides
    @Singleton
    fun provideTopRatedMovieRepository(
        dataSource: TopRatedMovieRemoteDataSource,
        @Named("topRatedLocalDataSource") localTopRated: HomeLocalDataSource<TopRatedLocal>,
        crashReporter: CrashReporter
    ): TopRatedMovieRepository =
        TopRatedMovieRepositoryImpl(
            topRatedMovieRemoteDataSource = dataSource,
            localTopRated = localTopRated,
            crashReporter = crashReporter
        )

    @Provides
    @Singleton
    fun provideTopRatedTvSeriesRepository(
        dataSource: TopRatedTvRemoteDataSource,
        @Named("topRatedLocalDataSource") localTopRated: HomeLocalDataSource<TopRatedLocal>,
        crashReporter: CrashReporter
    ): TopRatedTvSeriesRepository =
        TopRatedTvSeriesRepositoryImpl(
            topRatedTvRemoteDataSource = dataSource,
            topRatedTvShow = localTopRated,
            crashReporter = crashReporter
        )

    @Provides
    @Singleton
    fun provideTrendingRemoteDataSource(
        trendingApiService: TrendingApiService
    ): TrendingRemoteDataSource =
        TrendingRemoteDataSourceImpl(trendingApiService = trendingApiService)

    @Provides
    @Singleton
    fun provideTrendingRepository(
        dataSource: TrendingRemoteDataSource
    ): TrendingRepository = TrendingRepositoryImpl(trendingRemoteDataSource = dataSource)

    @Provides
    @Singleton
    fun provideActorRepository(
        dataSource: ActorDetailsRemoteDataSource
    ): ActorRepository = ActorRepositoryImpl(dataSource = dataSource)

    @Provides
    @Singleton
    fun provideDetailsRepository(
        tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
        reviewsRemoteDataSource: ReviewsRemoteDataSource
    ): TvShowRepository = TvShowRepositoryImpl(
        tvShowDetailsRemoteDataSource = tvShowDetailsRemoteDataSource,
        reviewsRemoteDataSource = reviewsRemoteDataSource
    )

    @Provides
    @Singleton
    fun provideMovieDetailsRepository(
        dataSource: MovieDetailsRemoteDataSource
    ): MovieDetailsRepository =
        MovieDetailsRepositoryImpl(movieDetailsRemoteDataSource = dataSource)

    @Provides
    @Singleton
    fun provideMovieVideoRepository(
        dataSource: MovieVideoProviderRemote
    ): MovieVideoProviderRepository = MovieVideoProviderRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideSearchRepository(
        genreInterestDao: GenreInterestDao,
        remoteDataSource: SearchRemoteDataSource,
        crashReporter: CrashReporter
    ): SearchRepository = SearchRepositoryImpl(
        genreInterestDao = genreInterestDao,
        remoteDataSource = remoteDataSource,
        crashReporter = crashReporter
    )

    @Provides
    @Singleton
    fun provideTvShowVideoProviderRepository(
        dataSource: TvShowVideoProviderRemote
    ): TvShowVideoProviderRepository =
        TvShowVideoProviderRepositoryImpl(tvShowVideoProviderRemote = dataSource)

    @Provides
    @Singleton
    fun provideUpComingRepository(
        upComingLocalDataSource: UpComingLocalDataSource,
        upComingRemoteDataSource: UpComingRemoteDataSource,
        crashReporter: FirebaseCrashReporter
    ): UpComingRepository = UpComingRepositoryImpl(
        upComingLocalDataSource = upComingLocalDataSource,
        upComingRemoteDataSource = upComingRemoteDataSource,
        crashReporter = crashReporter
    )

    @Provides
    @Singleton
    fun provideDisCoverRepository(
        dataSource: DiscoverRemoteDataSource
    ): DiscoverRepository = DiscoverRepositoryImpl(
        remoteDataSource = dataSource
    )


    @Provides
    @Singleton
    fun provideMovieRatingRepository(
        movieRatingRemoteDataSource: MovieRatingRemoteDataSource,
        authPreferences: AuthPreferences
    ): RatingRepository = RatingRepositoryImpl(
        movieRatingRemoteDataSource = movieRatingRemoteDataSource,
        authPreferences = authPreferences
    )
}
