package com.london.app.di

import com.london.domain.repo.SearchRepository
import com.london.presentation.screen.search.SearchViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ViewModelModule {
    @Single
    fun provideSearchViewModel(searchRepository: SearchRepository): SearchViewModel {
        return SearchViewModel(searchRepository)
    }
}