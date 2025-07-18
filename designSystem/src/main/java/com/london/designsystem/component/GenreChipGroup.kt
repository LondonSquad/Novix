package com.london.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun GenreChipGroup(
    availableGenres: List<Pair<Int, Int>>,
    selectedGenres: List<Int>,
    onGenreSelectionChanged: (List<Int>) -> Unit
) {
    Column(
        modifier = Modifier
            .height(130.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
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
}
