package com.london.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CustomReleasedYearSlider
import com.london.designsystem.component.GenreChipGroup
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberNovixModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.search.SearchInteractions
import com.london.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

data class FilterState(
    val availableGenres: List<Pair<Int, Int>>,
    val selectedGenres: List<Int>,
    val minimumImdbRating: Int,
    val isSheetVisible: Boolean,
    val releaseYearRange: ClosedFloatingPointRange<Float>
)

@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    filterInteractions: SearchInteractions,
    sheetState: SheetState = rememberNovixModalBottomSheetState(),
    filterState: FilterState,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(filterState.isSheetVisible) {
        if (filterState.isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    if (filterState.isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = filterInteractions::onFilterSheetDismiss,
            containerColor = NovixTheme.colors.surface,
            state = sheetState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.75f)
                    .padding(bottom = 24.dp)
            ) {
                FilterBottomSheetContent(
                    modifier = modifier,
                    filterInteractions = filterInteractions,
                    filterUiState = filterState,
                    onCloseClicked = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                filterInteractions.onFilterSheetDismiss()
                            }
                        }
                    },
                    onApplyClicked = {
                        filterInteractions.onApplyFilter(
                            filterState.selectedGenres,
                            filterState.minimumImdbRating,
                            filterState.releaseYearRange
                        )
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                filterInteractions.onFilterSheetDismiss()
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    filterInteractions: SearchInteractions,
    filterUiState: FilterState,
    onCloseClicked: () -> Unit,
    onApplyClicked: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.filter),
                    style = NovixTheme.typography.title.large,
                    color = NovixTheme.colors.title,
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = NovixTheme.colors.stroke,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onCloseClicked)
                        .padding(6.dp),
                    painter = painterResource(com.london.designsystem.R.drawable.cancel),
                    contentDescription = "Close filter",
                    tint = NovixTheme.colors.title
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.released_year),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            CustomReleasedYearSlider(
                yearRange = filterUiState.releaseYearRange,
                onYearRangeChange = filterInteractions::onReleaseYearRangeChange,
                minYear = 1950,
                maxYear = 2030
            )
        }

        item {
            Text(
                text = stringResource(R.string.genres),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            GenreChipGroup(
                availableGenres = filterUiState.availableGenres,
                selectedGenres = filterUiState.selectedGenres,
                onGenreSelectionChanged = filterInteractions::onGenreSelectedChange
            )
        }

        item {
            Text(
                text = stringResource(R.string.imdb_rating),
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        item {
            RatingBar(
                rating = filterUiState.minimumImdbRating,
                onRatingChanged = filterInteractions::onRatingChanged,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.apply),
                    hasLabel = true,
                    hasIcon = false,
                    isLoading = false,
                    icon = null,
                    onClick = onApplyClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlineButton(
                    text = stringResource(R.string.clear),
                    hasLabel = true,
                    icon = null,
                    hasIcon = false,
                    isLoading = false,
                    onClick = filterInteractions::onClearFilter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun FilterBottomSheetContentPreview() {
    NovixTheme {
        Box {
            FilterBottomSheetContent(
                filterInteractions = koinViewModel<SearchViewModel>(),
                filterUiState = FilterState(
                    availableGenres = listOf(
                        28 to R.string.action,
                        12 to R.string.action,
                        16 to R.string.action,
                        35 to R.string.action
                    ),
                    selectedGenres = emptyList(),
                    minimumImdbRating = 0,
                    isSheetVisible = true,
                    releaseYearRange = 1950f..2030f
                ),
                onCloseClicked = {},
                onApplyClicked = {},
            )
        }
    }
}