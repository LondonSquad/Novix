package com.london.presentation.shared.bookmarkSheet

sealed interface BookmarkSheetEffect {
    object NewListCreation : BookmarkSheetEffect
    object LoginNavigation : BookmarkSheetEffect
}
