package com.london.app.di

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.RemoteDataSource
import com.london.data.repository.SearchRepositoryImpl
import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class RepositoryModule {
    @Single
    fun provideSearchRepository(
        searchTvShowService: LocalDataSource<SearchTvShowLocal>,
        searchActorService: LocalDataSource<SearchActorsLocal>,
        searchMovieService: LocalDataSource<SearchMoviesLocal>,
        remoteDataSource: RemoteDataSource,
    ): SearchRepository {
        return SearchRepositoryImpl(
            searchTvShowService = searchTvShowService,
            searchActorService = searchActorService,
            searchMovieService = searchMovieService,
            remoteDataSource = remoteDataSource
        )
    }
}