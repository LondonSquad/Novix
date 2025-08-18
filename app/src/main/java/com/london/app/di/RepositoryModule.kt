package com.london.app.di

import com.london.data.repository.account.AccountRepositoryImpl
import com.london.data.repository.actor.ActorRepositoryImpl
import com.london.data.repository.authentication.AuthenticationRepositoryImpl
import com.london.data.repository.list.CustomMovieListRepositoryImpl
import com.london.data.repository.movie.MovieRepositoryImpl
import com.london.data.repository.recent.RecentSearchRepositoryImpl
import com.london.data.repository.recent.RecentViewedRepositoryImpl
import com.london.data.repository.recent.RecentWatchedRepositoryImpl
import com.london.data.repository.search.SearchRepositoryImpl
import com.london.data.repository.tvshow.TvShowRepositoryImpl
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.AccountRepository
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.AuthenticationRepository
import com.london.domain.repository.CustomMovieListRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.RecentRepository
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideAuthenticationRepository(
        implementation: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun provideRecentSearchRepository(
        implementation: RecentSearchRepositoryImpl
    ): RecentRepository<RecentSearch>

    @Binds
    @Singleton
    abstract fun provideRecentViewedRepository(
        implementation: RecentViewedRepositoryImpl
    ): RecentRepository<RecentViewed>

    @Binds
    @Singleton
    abstract fun provideRecentWatchedRepository(
        dataSource: RecentWatchedRepositoryImpl
    ): RecentWatchedRepository

    @Binds
    @Singleton
    abstract fun provideActorRepository(
        implementation: ActorRepositoryImpl
    ): ActorRepository

    @Binds
    @Singleton
    abstract fun provideDetailsRepository(
        implementation: TvShowRepositoryImpl
    ): TvShowRepository

    @Binds
    @Singleton
    abstract fun provideMovieDetailsRepository(
        implementation: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun provideSearchRepository(
        implementation: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun provideCustomMovieListsRepository(
        implementation: CustomMovieListRepositoryImpl
    ): CustomMovieListRepository

    @Binds
    @Singleton
    abstract fun provideAccountRepository(
        implementation: AccountRepositoryImpl
    ): AccountRepository
}
