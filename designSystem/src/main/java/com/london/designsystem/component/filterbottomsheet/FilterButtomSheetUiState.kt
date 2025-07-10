package com.london.designsystem.component.filterbottomsheet


data class FilterBottomSheetUiState(
    val timeSearchBetween :Pair<Int,Int>,
    val genres :List<String>,
    val imdbRating:String,
)