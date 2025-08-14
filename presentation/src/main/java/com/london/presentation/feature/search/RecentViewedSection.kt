package com.london.presentation.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.R
import com.london.presentation.shared.HomeCard

@Composable
fun RecentViewedSection(
    recentViewed: List<RecentViewed>,
    onClearAll: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
) {
    SectionHeader(
        text = stringResource(R.string.recent_viewed),
        hasGetAll = true,
        hasIcon = false,
        getAllText = stringResource(R.string.clear_all),
        onClick = onClearAll,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(recentViewed) { item ->
            HomeCard(
                imageUrl = item.imageUrl,
                isSaved = false,
                onSaveClick = { },
                modifier = Modifier.clickable {
                    when (item.type) {
                        MediaType.Movie -> onNavigateToMovieDetails(item.id)
                        MediaType.TvShow -> onNavigateToTvShowDetails(item.id)
                    }
                },
                isDarkMode = NovixTheme.isThemeDark
            )
        }
    }
}
