package com.london.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreChipGroup(
    availableGenres: List<Pair<Int, Int>>,
    selectedGenres: List<Int>,
    onGenreSelectionChanged: (List<Int>) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableGenres.forEach { (genreId, genreName) ->
            NovixChip(
                text = stringResource(genreName),
                isSelected = selectedGenres.contains(genreId),
                onClick = {
                    val updatedSelection = if (selectedGenres.contains(genreId)) {
                        selectedGenres - genreId
                    } else {
                        selectedGenres + genreId
                    }
                    onGenreSelectionChanged(updatedSelection)
                }
            )
        }
    }
}
