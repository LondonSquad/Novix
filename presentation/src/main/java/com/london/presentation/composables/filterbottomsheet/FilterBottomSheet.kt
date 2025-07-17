package com.london.presentation.composables.filterbottomsheet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CustomReleasedYearSlider
import com.london.designsystem.component.GenreChipGroup
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onDismissRequest: () -> Unit
) {
    val filterUiState by viewModel.filterUiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismissRequest()
            }
        },
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = NovixTheme.colors.body,
            )
        },
        containerColor = NovixTheme.colors.surface
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            FilterBottomSheetContent(
                modifier = modifier,
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                onApplyFilters = { selectedGenres, minimumRating, releaseYearRange ->
                    viewModel.onApplyFilter(selectedGenres, minimumRating, releaseYearRange)
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                onClearFilters = { viewModel.onClearFilter() },
                availableGenres = filterUiState.availableGenresWithNames,
                selectedGenres = filterUiState.selectedGenres,
                imdbRating = filterUiState.imdbRating,
                releaseYearRange = filterUiState.releaseYearRange,
                onReleaseYearRangeChange = viewModel::onReleaseYearRangeChange,
                onGenreSelectedChange = viewModel::onGenreSelectedChange,
                onRatingChanged = viewModel::onRatingChanged,
            )
        }
    }
}

@Composable
private fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    availableGenres: List<Pair<Int, Int>>,
    onDismissRequest: () -> Unit,
    onApplyFilters: (
        selectedGenres: List<Int>,
        minimumRating: Int,
        releaseYearRange: ClosedFloatingPointRange<Float>
    ) -> Unit,
    onClearFilters: () -> Unit,
    selectedGenres: List<Int>,
    imdbRating: Int,
    releaseYearRange: ClosedFloatingPointRange<Float>,
    onReleaseYearRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onGenreSelectedChange: (List<Int>) -> Unit,
    onRatingChanged: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(com.london.designsystem.R.drawable.cancel),
                    contentDescription = "Close filter",
                    modifier = Modifier.padding(6.dp),
                    tint = NovixTheme.colors.title
                )
            }
        }

        Text(
            text = stringResource(R.string.released_year),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        CustomReleasedYearSlider(
            yearRange = releaseYearRange,
            onYearRangeChange = onReleaseYearRangeChange,
            minYear = 1950,
            maxYear = 2030
        )

        Text(
            text = stringResource(R.string.genres),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        GenreChipGroup(
            availableGenres = availableGenres,
            selectedGenres = selectedGenres,
            onGenreSelectionChanged = onGenreSelectedChange
        )

        Text(
            text = stringResource(R.string.imdb_rating),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(top = 24.dp)
        )

        RatingBar(
            rating = imdbRating,
            onRatingChanged = onRatingChanged,
            modifier = Modifier.padding(top = 8.dp)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .padding(top = 24.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.apply),
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                isDisabled = false,
                icon = null,
                onClick = {
                    onApplyFilters(
                        selectedGenres,
                        imdbRating,
                        releaseYearRange
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlineButton(
                text = stringResource(R.string.clear),
                hasLabel = true,
                icon = null,
                hasIcon = false,
                isLoading = false,
                isDisabled = false,
                onClick = {
                    onClearFilters()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun FilterBottomSheetContentPreview() {
    NovixTheme {
        Surface {
            FilterBottomSheetContent(
                onDismissRequest = {},
                onApplyFilters = { selectedGenres, rating, range ->
                    println("Apply filters clicked with genres=$selectedGenres, rating=$rating, yearRange=$range")
                },
                onClearFilters = {
                    println("Clear filters clicked")
                },
                availableGenres = listOf(
                    28 to R.string.action,
                    12 to R.string.action,
                    16 to R.string.action,
                    35 to R.string.action
                ),
                selectedGenres = listOf(),
                imdbRating = 0,
                releaseYearRange = 1950f..2030f,
                onReleaseYearRangeChange = {},
                onGenreSelectedChange = { },
                onRatingChanged = {},
            )
        }
    }
}