package com.london.presentation.shared.bookmarkSheet

import com.london.domain.entity.MovieList

fun List<MovieList>.toBookmarkUiLists(): List<BookmarkUiList> =
    map { movieList ->
        BookmarkUiList(
            id = movieList.id,
            name = movieList.name,
            itemCount = movieList.moviesCount
        )
    }