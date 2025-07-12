package com.london.presentation.composables.filterbottomsheet


data class FilterBottomSheetUiState(
    val selectedGenre: String? = null,
    val imdbRating: Int = 0,
    val yearRange: ClosedFloatingPointRange<Float> = 1950f..2030f
)