package com.london.app.di

import com.london.data.repo.DummySearchRepositoryImpl
import com.london.domain.repo.SearchRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class RepositoryModule {
    @Single
    fun provideSearchRepository(): SearchRepository {
        return DummySearchRepositoryImpl()
    }
}