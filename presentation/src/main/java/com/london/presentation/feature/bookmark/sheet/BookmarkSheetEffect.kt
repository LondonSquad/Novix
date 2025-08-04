package com.london.presentation.feature.bookmark.sheet

sealed interface BookmarkSheetEffect {
    object NewListCreation : BookmarkSheetEffect
    object ItemSuccessfulAddition : BookmarkSheetEffect
    object ItemFailedAddition : BookmarkSheetEffect
}