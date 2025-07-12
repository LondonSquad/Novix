package com.london.app.di

import com.london.domain.repository.SearchRepository
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetTvShowsUseCase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class UseCaseModule {
    @Single
    fun provideGetActorsUseCase(repository: SearchRepository) = GetActorsUseCase(repository)
    @Single
    fun provideGetMoviesUseCase(repository: SearchRepository) = GetMoviesUseCase(repository)
    @Single
    fun provideGetTvShowsUseCase(repository: SearchRepository) = GetTvShowsUseCase(repository)
}