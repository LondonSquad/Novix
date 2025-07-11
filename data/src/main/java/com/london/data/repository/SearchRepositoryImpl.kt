package com.london.data.repository

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.RemoteDataSource

class SearchRepositoryImpl(
    private val searchTvShowService: LocalDataSource<SearchTvShowLocal>,
    private val searchActorService: LocalDataSource<SearchActorsLocal>,
    private val searchMovieService: LocalDataSource<SearchMoviesLocal>,
    private val remoteDataSource: RemoteDataSource
)