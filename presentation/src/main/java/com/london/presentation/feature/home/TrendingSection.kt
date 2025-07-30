package com.london.presentation.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@Composable
fun TrendingSection(
    onMoviesClick: () -> Unit,
    onTvShowsClick: () -> Unit,
    onActorsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface)
    ) {
        Text(
            text = stringResource(R.string.What_you_want_to_watch),
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .zIndex(1f)
        )

        LazyRow(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(NovixTheme.colors.surface),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            item {
                CategoryCard(
                    title = stringResource(R.string.Movies),
                    icon = painterResource(id = R.drawable.icon_movie),
                    startColor = NovixTheme.colors.primary,
                    endColor = NovixTheme.colors.darkCocoa,
                    imageWidth = 60.dp,
                    imageHeight = 64.dp,
                    onClick = onMoviesClick,
                )
            }

            item {
                CategoryCard(
                    title = stringResource(R.string.TV_Shows),
                    icon = painterResource(id = R.drawable.icon_tvshow),
                    startColor = NovixTheme.colors.secondary,
                    endColor = NovixTheme.colors.deepCrimson,
                    imageWidth = 88.46.dp,
                    imageHeight = 64.dp,
                    onClick = onTvShowsClick,
                )
            }

            item {
                CategoryCard(
                    title = stringResource(R.string.actors),
                    icon = painterResource(id = R.drawable.icon_actor),
                    startColor = NovixTheme.colors.tealBlue,
                    endColor = NovixTheme.colors.oceanDark,
                    imageWidth = 56.49.dp,
                    imageHeight = 64.dp,
                    onClick = onActorsClick,
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    title: String,
    icon: Painter,
    startColor: Color,
    endColor: Color,
    imageWidth: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 64.dp
) {
    val gradient = Brush.linearGradient(
        colors = listOf(startColor, endColor)
    )
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    Box(
        modifier = modifier
            .height(116.dp)
            .widthIn(min = 104.dp)
            .background(
                brush = gradient,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = icon,
            contentDescription = title,
            modifier = Modifier
                .size(width = imageWidth, height = imageHeight)
                .align(Alignment.TopStart)
                .padding(start = 4.dp)
                .offset(y = (-12).dp)
                .then(if (isRtl) Modifier.scale(-1f, 1f) else Modifier)
        )

        Text(
            text = title,
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, end = 8.dp, bottom = 7.dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TrendingSection(
        onMoviesClick = {},
        onTvShowsClick = {},
        onActorsClick = {}
    )
}