package com.london.data.repo

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.remote.RemoteDataSource

class SearchRepositoryImpl(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource: RemoteDataSource
)