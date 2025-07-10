package com.london.designsystem.component.filterbottomsheet

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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.NovixChip
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    uiState: FilterBottomSheetUiState,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onApply: () -> Unit = {},
    onDismissRequest: () -> Unit
) {

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
            uiState = uiState,
            onCancel = onCancel,
            onApply = onApply,
            modifier = modifier
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    uiState: FilterBottomSheetUiState,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(
                bottom = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.filter),
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
            )

            Spacer(Modifier.fillMaxWidth(1f))


            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onCancel() }
                    .clip(RoundedCornerShape(8.dp))
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.cancel),
                    contentDescription = "cancel the bottom Sheet", modifier.padding(8.dp)
                )
            }
        }

        Text(
            text = stringResource(R.string.released_year),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
        )

        //todo release year animation picker


        Text(
            text = stringResource(R.string.genres),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
        )
        //todo without internet i don't know how to group them
        NovixChip("Comedy")


        Text(
            text = stringResource(R.string.imdb_rating),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
        )

        //todo IMDb rating animation picker


        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.End)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .background(NovixTheme.colors.primary)
                    .clickable { onApply() }
                    .clip(RoundedCornerShape(8.dp))
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
                    .clickable { onApply() }
                    .clip(RoundedCornerShape(8.dp))
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


@ThemePreviews
@Composable
private fun PreviewTaskDetail() {
    FilterBottomSheet(
        uiState = FilterBottomSheetUiState(
            timeSearchBetween = Pair(1, 3),
            genres = listOf("", "", ""),
            imdbRating = "3.4"
        ),
        onCancel = {},
        onApply = {},
        onDismissRequest = {},
    )
}