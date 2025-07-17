package com.london.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
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
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(availableGenres) { (genreId, genreName) ->
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
