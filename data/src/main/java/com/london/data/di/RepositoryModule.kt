package com.london.data.di

import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.local.model.search.SearchTvShowLocal
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.source.LocalDataSource
import com.london.data.local.source.recent.RecentDataSource
import com.london.data.local.source.recent.watched.RecentWatchedDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSource
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSource
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.details.videoprovider.movie.MovieVideoProviderRemote
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.repository.ActorRepositoryImpl
import com.london.data.repository.DetailsRepositoryImpl
import com.london.data.repository.MovieDetailsRepositoryImpl
import com.london.data.repository.MovieVideoProviderRepositoryImpl
import com.london.data.repository.SearchRepositoryImpl
import com.london.data.repository.TvShowVideoProviderRepositoryImpl
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import com.london.data.repository.popular.PopularRepositoryImpl
import com.london.data.repository.recent.RecentSearchRepositoryImpl
import com.london.data.repository.recent.RecentViewedRepositoryImpl
import com.london.data.repository.recent.RecentWatchedRepositoryIml
import com.london.data.repository.toprated.TopRatedMovieRepositoryImpl
import com.london.data.repository.toprated.TopRatedTvSeriesRepositoryImpl
import com.london.data.repository.trending.TrendingRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.domain.repository.RecentWatchedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthenticationRepository(
        authRemoteDataSource: AuthenticationRemoteDataSource,
        authPreferences: AuthPreferences
    ) = AuthenticationRepositoryImpl(authRemoteDataSource, authPreferences)

    @Provides
    fun providePopularRepository(
        dataSource: PopularRemoteDataSource
    ) = PopularRepositoryImpl(popularRemoteDataSource = dataSource)

    @Provides
    fun provideRecentSearchRepository(
        dataSource: RecentDataSource<RecentSearchLocal>
    ) = RecentSearchRepositoryImpl(recentSearchLocalDataSource = dataSource)

    @Provides
    fun provideRecentViewedRepository(
        dataSource: RecentDataSource<RecentViewedLocal>
    ) = RecentViewedRepositoryImpl(recentRecentViewedLocalDataSource = dataSource)

    @Provides
    fun provideRecentWatchedRepository(
        recentWatchedMoviesDataSource: RecentWatchedDataSource<RecentWatchedMovieLocal>,
        recentWatchedTvShowsDataSource: RecentWatchedDataSource<RecentWatchedTvShowLocal>
    ): RecentWatchedRepository = RecentWatchedRepositoryIml(
        recentWatchedMoviesDataSource = recentWatchedMoviesDataSource,
        recentWatchedTvShowsDataSource = recentWatchedTvShowsDataSource
    )

    @Provides
    fun provideTopRatedMovieRepository(
        dataSource: TopRatedMovieRemoteDataSource
    ) = TopRatedMovieRepositoryImpl(topRatedMovieRemoteDataSource = dataSource)

    @Provides
    fun provideTopRatedTvSeriesRepository(
        dataSource: TopRatedTvRemoteDataSource
    ) = TopRatedTvSeriesRepositoryImpl(topRatedTvRemoteDataSource = dataSource)

    @Provides
    fun provideTrendingRepository(
        dataSource: TrendingRemoteDataSource
    ) = TrendingRepositoryImpl(trendingRemoteDataSource = dataSource)

    @Provides
    fun provideActorRepository(
        dataSource: ActorDetailsRemoteDataSource
    ) = ActorRepositoryImpl(dataSource = dataSource)

    @Provides
    fun provideDetailsRepository(
        tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
        reviewsRemoteDataSource: ReviewsRemoteDataSource
    ) = DetailsRepositoryImpl(
        tvShowDetailsRemoteDataSource = tvShowDetailsRemoteDataSource,
        reviewsRemoteDataSource = reviewsRemoteDataSource
    )

    @Provides
    fun provideMovieDetailsRepository(
        dataSource: MovieDetailsRemoteDataSource
    ) = MovieDetailsRepositoryImpl(movieDetailsRemoteDataSource = dataSource)

    @Provides
    fun provideMovieVideoRepository(
        dataSource: MovieVideoProviderRemote
    ) = MovieVideoProviderRepositoryImpl(dataSource)

    @Provides
    fun provideSearchRepository(
        localTvShowDataSource: LocalDataSource<SearchTvShowLocal>,
        localActorDataSource: LocalDataSource<SearchActorsLocal>,
        localMovieDataSource: LocalDataSource<SearchMoviesLocal>,
        genreInterestDao: GenreInterestDao,
        remoteDataSource: SearchRemoteDataSource,
        crashReporter: CrashReporter
    ) = SearchRepositoryImpl(
        localTvShowDataSource = localTvShowDataSource,
        localActorDataSource = localActorDataSource,
        localMovieDataSource = localMovieDataSource,
        genreInterestDao = genreInterestDao,
        remoteDataSource = remoteDataSource,
        crashReporter = crashReporter
    )

    @Provides
    fun provideTvShowVideoProviderRepository(
        dataSource: TvShowVideoProviderRemote
    ) = TvShowVideoProviderRepositoryImpl(tvShowVideoProviderRemote = dataSource)
}
