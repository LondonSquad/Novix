package com.london.data.repository

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.remote.search.RemoteDataSource

class SearchRepositoryImpl(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource: RemoteDataSource
)