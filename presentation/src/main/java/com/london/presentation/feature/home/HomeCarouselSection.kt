package com.london.presentation.feature.home

import androidx.annotation.StringRes
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
import com.london.designsystem.component.carousel.m3.CarouselState
import com.london.designsystem.component.carousel.m3.rememberCarouselState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable
import com.london.designsystem.utils.string
import com.london.domain.entity.recent.MediaType

@Composable
fun HomeCarouselSection(
    modifier: Modifier = Modifier,
    uiMediaList: List<HomeUiMedia>,
    @StringRes sectionName: Int,
    carouselState: CarouselState = rememberCarouselState { uiMediaList.size },
    onSaveClick: (Int) -> Unit,
    onCardClick: (Int, MediaType) -> Unit,
    isLoading: Boolean = false,
    onAllClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeCarouselDefaults.SECTION_VERTICAL_SPACING)
    ) {
        SectionHeader(
            modifier = Modifier.padding(horizontal = HomeCarouselDefaults.HORIZONTAL_PADDING),
            text = sectionName.string,
            isLoading = isLoading,
            hasGetAll = true,
            hasIcon = true,
            onClick = onAllClick
        )

        HeroCarousel(
            modifier = Modifier
                .height(HomeCarouselDefaults.CAROUSEL_HEIGHT)
                .padding(start = HomeCarouselDefaults.CAROUSEL_START_PADDING),
            carouselState = carouselState,
            heroItemSize = HomeCarouselDefaults.HERO_ITEM_SIZE,
            smallItemSize = HomeCarouselDefaults.SMALL_ITEM_SIZE,
            itemSpacing = HomeCarouselDefaults.ITEM_SPACING,
            contentPadding = PaddingValues(end = HomeCarouselDefaults.CONTENT_END_PADDING)
        ) { index ->
            val mediaItem = uiMediaList[index]
            HomeCard(
                modifier = Modifier
                    .noRippleClickable {
                        onCardClick(
                            mediaItem.id,
                            mediaItem.mediaType
                        )
                    }
                    .maskClip(RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS))
                    .maskBorder(
                        border = BorderStroke(
                            width = HomeCarouselDefaults.BORDER_WIDTH,
                            color = NovixTheme.colors.stroke
                        ),
                        shape = RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS)
                    ),
                imageUrl = mediaItem.posterUrl,
                onSaveClick = { onSaveClick(mediaItem.id) },
                hasSaveIcon = isHero
            )
        }
    }
}

private object HomeCarouselDefaults {
    val SECTION_VERTICAL_SPACING = 12.dp
    val HORIZONTAL_PADDING = 16.dp
    val CONTENT_END_PADDING = 16.dp
    val CAROUSEL_START_PADDING = 16.dp
    val CAROUSEL_HEIGHT = 210.dp
    val HERO_ITEM_SIZE = 158.dp
    val SMALL_ITEM_SIZE = 74.dp
    val ITEM_SPACING = 8.dp
    val CARD_CORNER_RADIUS = 12.dp
    val BORDER_WIDTH = 1.dp
}