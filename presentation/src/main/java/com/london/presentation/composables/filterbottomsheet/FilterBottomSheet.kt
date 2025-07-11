package com.london.presentation.composables.filterbottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    var uiState by remember { mutableStateOf(FilterBottomSheetUiState()) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier.background(NovixTheme.colors.body),
            )
        },
        containerColor = NovixTheme.colors.surface
    ) {

        FilterBottomSheetContent(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onApply = { selectedGenre, imdbRating, yearRange ->
                uiState = uiState.copy(
                    selectedGenre = selectedGenre,
                    imdbRating = imdbRating,
                    yearRange = yearRange
                )
                onDismissRequest()
            },
            onClear = {
                uiState = FilterBottomSheetUiState()
            }
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    modifier: Modifier = Modifier, genres: List<String> = listOf(
        stringResource(R.string.filter),
        stringResource(R.string.action),
        stringResource(R.string.drama),
        stringResource(R.string.comedy),
        stringResource(R.string.sci_fi),
        stringResource(R.string.romance),
        stringResource(R.string.crime),
        stringResource(R.string.adventure),
        stringResource(R.string.documentary),
    ),
    onDismissRequest: () -> Unit,
    onApply: (selected: String?, imdbRating: Int, yearRange: ClosedFloatingPointRange<Float>) -> Unit,
    onClear: () -> Unit
) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    var imdbRating by rememberSaveable { mutableIntStateOf(7) }
    var yearRange by rememberSaveable { mutableStateOf(1980f..2025f) }

    Column(
        modifier = modifier
            .padding(
                bottom = 24.dp,
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
                    contentDescription = "Cancel the bottom sheet",
                    modifier = Modifier.padding(6.dp),
                    tint = NovixTheme.colors.title
                )
            }
        }

        Text(
            text = stringResource(R.string.released_year),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CustomReleasedYearSlider(
            yearRange = yearRange,
            onYearRangeChange = { yearRange = it },
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
            genres = genres,
            selectedGenre = selected,
            onGenreSelected = { selected = it }
        )

        Text(
            text = stringResource(R.string.imdb_rating),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(top = 24.dp)
        )

        RatingBar(
            rating = imdbRating,
            onRatingChanged = { imdbRating = it },
            modifier = Modifier.padding(top = 8.dp)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.End)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NovixTheme.colors.primary)
                    .clickable {
                        onApply(
                            selected,
                            imdbRating,
                            yearRange
                        )
                    }
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.apply),
                    style = NovixTheme.typography.label.large,
                    color = NovixTheme.colors.onPrimary,
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClear() }
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.clear),
                    style = NovixTheme.typography.label.large,
                    color = NovixTheme.colors.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBottomSheetContentPreview() {
    NovixTheme {
        Surface {
            FilterBottomSheetContent(
                onDismissRequest = {},
                onApply = { selected, rating, range ->
                    println("Apply clicked with genre=$selected, rating=$rating, yearRange=$range")
                },
                onClear = {
                    println("Clear clicked")
                }
            )
        }
    }
}