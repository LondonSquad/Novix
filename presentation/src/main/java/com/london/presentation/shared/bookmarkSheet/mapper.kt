package com.london.presentation.shared.bookmarkSheet

import androidx.paging.PagingData
import androidx.paging.map
import com.london.domain.entity.MovieList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<PagingData<MovieList>>.toBookmarkUiLists(): Flow<PagingData<BookmarkUiList>> =
    map { pagingData ->
        pagingData.map { list ->
            BookmarkUiList(
                id = list.id,
                name = list.name,
                itemCount = list.moviesCount.toUShort()
            )
        }
    }