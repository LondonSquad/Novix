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
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.actor.ActorDetailsRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.remote.source.movie.MovieRemoteDataSource
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.remote.source.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.repository.account.AccountRepositoryImp
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import com.london.data.repository.list.CustomMovieListRepositoryImpl
import com.london.data.repository.movie.MovieRepositoryImpl
import com.london.data.repository.recent.RecentSearchRepositoryImpl
import com.london.data.repository.recent.RecentViewedRepositoryImpl
import com.london.data.repository.recent.RecentWatchedRepositoryIml
import com.london.data.repository.actor.ActorRepositoryImpl
import com.london.data.repository.search.SearchRepositoryImpl
import com.london.data.repository.tvshow.TvShowRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.domain.AppPreferencesService
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.AccountRepository
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.AuthRepository
import com.london.domain.repository.CustomMovieListRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.RecentRepository
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
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
        accountRemoteDataSource: AccountRemoteDataSource,
        authPreferences: AuthPreferences
    ): AuthRepository =
        AuthenticationRepositoryImpl(authRemoteDataSource, accountRemoteDataSource, authPreferences)

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
    fun provideActorRepository(
        dataSource: ActorDetailsRemoteDataSource
    ): ActorRepository = ActorRepositoryImpl(dataSource = dataSource)

    @Provides
    @Singleton
    fun provideDetailsRepository(
        tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
        @Named("topRatedLocalDataSource") localTopRated: HomeLocalDataSource<TopRatedLocal>,
        @Named("popularLocalDataSource") homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
        crashReporter: CrashReporter,
        authPreferences: AuthPreferences
    ): TvShowRepository = TvShowRepositoryImpl(
        tvShowDetailsRemoteDataSource = tvShowDetailsRemoteDataSource,
        authPreferences = authPreferences,
        homeLocalDataSource = homeLocalDataSource,
        localTopRated = localTopRated,
        crashReporter = crashReporter
    )

    @Provides
    @Singleton
    fun provideMovieDetailsRepository(
        dataSource: MovieRemoteDataSource,
        authPreferences: AuthPreferences,
        upComingLocalDataSource: UpComingLocalDataSource,
        @Named("topRatedLocalDataSource") localTopRated: HomeLocalDataSource<TopRatedLocal>,
        @Named("popularLocalDataSource") homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
        crashReporter: CrashReporter
    ): MovieRepository =
        MovieRepositoryImpl(
            movieRemoteDataSource = dataSource,
            authPreferences = authPreferences,
            homeLocalDataSource = homeLocalDataSource,
            localTopRated = localTopRated,
            upComingLocalDataSource = upComingLocalDataSource,
            crashReporter = crashReporter
        )

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
    fun provideCustomMovieListsRepository(
        dataSource: CustomMovieListsRemoteDataSource,
        authPreferences: AuthPreferences,
        preferencesService: AppPreferencesService
    ): CustomMovieListRepository = CustomMovieListRepositoryImpl(
        remoteDataSource = dataSource,
        authPreferences = authPreferences,
        preferencesService = preferencesService
    )

    @Provides
    @Singleton
    fun provideAccountRepository(
        authPreferences: AuthPreferences,
        accountRemoteDataSource: AccountRemoteDataSource
    ): AccountRepository = AccountRepositoryImp(
        authPreferences = authPreferences,
        accountRemoteDataSource = accountRemoteDataSource
    )

}
