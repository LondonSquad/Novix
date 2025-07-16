package com.london.app.di

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.local.recentsearch.RecentSearchDataSource
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.datasource.remote.search.RemoteDataSource
import com.london.data.datasource.util.CrashReporter
import com.london.data.datasource.util.FirebaseCrashReporter
import com.london.data.repository.ActorRepositoryImpl
import com.london.data.repository.DetailsRepositoryImpl
import com.london.data.repository.MovieDetailsRepoImpl
import com.london.data.repository.RecentRepositoryImpl
import com.london.data.repository.SearchRepositoryImpl
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.DetailsRepository
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.RecentRepository
import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class RepositoryModule {
    @Single
    fun provideSearchRepository(
        @Named("tvShow") tvShowLocalDataSource: LocalDataSource<SearchTvShowLocal>,
        @Named("actor") actorLocalDataSource: LocalDataSource<SearchActorsLocal>,
        @Named("movie") movieLocalDataSource: LocalDataSource<SearchMoviesLocal>,
        remoteDataSource: RemoteDataSource,
        crashReporter: CrashReporter
    ): SearchRepository {
        return SearchRepositoryImpl(
            searchTvShowService = tvShowLocalDataSource,
            searchActorService = actorLocalDataSource,
            searchMovieService = movieLocalDataSource,
            remoteDataSource = remoteDataSource,
            crashReporter = crashReporter
        )
    }

    @Single
    fun provideDetailsRepository(
        tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    ): DetailsRepository {
        return DetailsRepositoryImpl(tvShowDetailsRemoteDataSource)
    }

    @Single
    fun provideRecentRepository(
        recentSearchDataSource: RecentSearchDataSource
    ): RecentRepository {
        return RecentRepositoryImpl(recentSearchDataSource)
    }

    @Single
    fun provideActorRepository(
        actorDetailsRemoteDataSource: ActorDetailsRemoteDataSource,
    ): ActorRepository {
        return ActorRepositoryImpl(actorDetailsRemoteDataSource)
    }

    @Single
    fun provideCrashReporter(): CrashReporter = FirebaseCrashReporter()

    @Single
    fun provideMovieDetailsRepository(
        movieDetailsRemote: MovieDetailsRemote,
    ): MovieDetailsRepository =
        MovieDetailsRepoImpl(movieDetailsRemote)
}