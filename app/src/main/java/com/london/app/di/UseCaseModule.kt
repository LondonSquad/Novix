package com.london.app.di

import com.london.domain.repository.DetailsRepository
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.RecentRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.usecase.AddToRecentSearchUseCase
import com.london.domain.usecase.ClearRecentSearchUseCase
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetMovieCastUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetRecentSearchUseCase
import com.london.domain.usecase.GetTvShowDetails
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

    @Single
    fun provideGetTvShowDetails(repository: DetailsRepository) = GetTvShowDetails(repository)

    @Single
    fun provideGetCastById(repository: DetailsRepository) = GetCastById(repository)

    @Single
    fun provideGetImagesById(repository: DetailsRepository) = GetImagesById(repository)

    @Single
    fun provideGetRecentSearchUseCase(repository: RecentRepository) =
        GetRecentSearchUseCase(repository)

    @Single
    fun provideAddToRecentSearchUseCase(repository: RecentRepository) =
        AddToRecentSearchUseCase(repository)

    @Single
    fun provideClearRecentSearchUseCase(repository: RecentRepository) =
        ClearRecentSearchUseCase(repository)


    @Single
    fun provideMovieCastUseCase(
        movieDetailsRepository: MovieDetailsRepository
    ): GetMovieCastUseCase =
         GetMovieCastUseCase(movieDetailsRepository)

}