package com.london.presentation.feature.search.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.presentation.feature.search.SearchCategory

@Composable
fun SearchChipsRow(
    selected: SearchCategory,
    onSelect: (SearchCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SearchCategory.entries.forEach { category ->
            NovixChip(
                text = stringResource(category.title),
                isSelected = selected == category,
                onClick = {
                    if (selected != category) {
                        onSelect(category)
                    }
                }
            )
        }
    }
}
