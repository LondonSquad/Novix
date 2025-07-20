package com.london.presentation.composables.filterbottomsheet

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CustomReleasedYearSlider
import com.london.designsystem.component.GenreChipGroup
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onDismissRequest: () -> Unit
) {
    val filterUiState by viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    var isSheetHidden = remember { true }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                isSheetHidden=false
                onDismissRequest()
            }
        },
        containerColor = NovixTheme.colors.surface
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(max = (screenHeightDp * 0.75f).dp)
                .padding(bottom = 24.dp)
        ) {
            FilterBottomSheetContent(
                modifier = modifier,
                onDismissRequest = {
                    scope.launch {
                        isSheetHidden=false
                        onDismissRequest()
                    }
                },
                onApplyFilters = { selectedGenres, minimumRating, releaseYearRange ->
                    viewModel.onApplyFilter(selectedGenres, minimumRating, releaseYearRange)
                    scope.launch {
                        isSheetHidden=false
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
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
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
                yearRange = releaseYearRange,
                onYearRangeChange = onReleaseYearRangeChange,
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
                availableGenres = availableGenres,
                selectedGenres = selectedGenres,
                onGenreSelectionChanged = onGenreSelectedChange
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
                rating = imdbRating,
                onRatingChanged = onRatingChanged,
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
}

@Preview
@Composable
fun FilterBottomSheetContentPreview() {
    NovixTheme {
        Box {
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