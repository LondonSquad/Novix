package com.london.presentation.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.carousel.HeroCarousel
import com.london.designsystem.component.carousel.isHero
import com.london.designsystem.component.carousel.m3.rememberCarouselState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.noRippleClickable
import com.london.designsystem.utils.string
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R

@Composable
fun TopRatedSection(
    modifier: Modifier = Modifier,
    topRatedUiMediaList: List<HomeUiMedia>,
    onSaveClick: (Int) -> Unit,
    onCardClick: (Int, MediaType) -> Unit,
    onAllClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = R.string.top_rated.string,
            hasGetAll = true,
            hasIcon = true,
            onClick = onAllClick
        )

        HeroCarousel(
            modifier = Modifier
                .height(210.dp)
                .padding(start = 16.dp),
            carouselState = rememberCarouselState { topRatedUiMediaList.size },
            heroItemSize = 158.dp,
            smallItemSize = 74.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(end = 16.dp)
        ) { index ->
            val mediaItem = topRatedUiMediaList[index]
            HomeCard(
                modifier = Modifier
                    .noRippleClickable {
                        onCardClick(
                            mediaItem.id,
                            mediaItem.mediaType
                        )
                    }
                    .maskClip(RoundedCornerShape(12.dp))
                    .maskBorder(
                        border = BorderStroke(width = 1.dp, color = NovixTheme.colors.stroke),
                        shape = RoundedCornerShape(12.dp)
                    ),
                imageUrl = mediaItem.posterUrl,
                onSaveClick = { onSaveClick(mediaItem.id) },
                hasSaveIcon = isHero
            )
        }
    }


}

@Composable
@ThemePreviews
private fun Preview() {
//    TopRatedSection()
}