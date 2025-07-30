package com.london.presentation.feature.details.tvshow.episodedetails

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.FooterSection
import com.london.presentation.shared.RatingItem
import com.london.presentation.utils.Listen
import com.london.presentation.utils.openUrl
import com.london.presentation.utils.toLocalizedNumbers
import org.koin.androidx.compose.koinViewModel
import com.london.designsystem.R as Res

@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCast: (Int) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            EpisodeDetailsEffect.NavigationBack -> onNavigateBack()
            is EpisodeDetailsEffect.NavigateToCast -> onNavigateToCast(currentEffect.episodeId)
        }
    }

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = false,
        content = {
            EpisodeDetailsScreenContent(
                uiState = uiState,
                episodeDetailsContract = viewModel,
                onNavigateToCast = onNavigateToCast
            )
        }
    )
}

@Composable
fun EpisodeDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState,
    episodeDetailsContract: EpisodeDetailsContract,
    onNavigateToCast: (Int) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val lazyListState = rememberLazyListState()
    val shouldShowBackground by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemScrollOffset > 40f ||
                    lazyListState.firstVisibleItemIndex > 0
        }
    }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (shouldShowBackground) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {

        TopBar(
            onBackClick = episodeDetailsContract::onBackClicked,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
                )
                .padding(horizontal = 16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                ),
            onClickOption1 = { /*todo on click on save*/ },
            option1Icon = com.london.designsystem.R.drawable.icon_remove,
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                val images = uiState.tvImages
                if (images != null) {
                    CustomBackDropImagePager(
                        images = images.map { it.fileUrl }
                    )
                }
            }

            item {
                HeaderDetailsCard(
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)

                            val yOffsetPx = with(this) { 44.dp.roundToPx() }
                            val adjustedHeight = (placeable.height - yOffsetPx).coerceAtLeast(0)

                            layout(placeable.width, adjustedHeight) {
                                placeable.placeRelative(0, -yOffsetPx)
                            }
                        }
                        .padding(start = 16.dp, end = 16.dp)
                        .heightIn(min = 158.dp)
                        .border(
                            width = 1.dp,
                            color = NovixTheme.colors.stroke,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(NovixTheme.colors.surface)
                )
            }

            item {
                OverviewSection(
                    uiState = uiState,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            }

            // Guests of honor section
            val guestStars = uiState.guestStars
            if (guestStars.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.guests_of_honor),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 9.dp)
                    )
                }

                items(
                    items = guestStars,
                ) { member ->
                    ActorItem(
                        actorName = member.name,
                        characterName = member.characterName,
                        imageRes = member.profileUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onNavigateToCast(member.id) }

                    )
                }

                item {
                    Spacer(Modifier.height(30.dp))
                }
            }
        }
        FooterSection(
            haveTrailer = uiState.episodeHaveTrailer,
            modifier = Modifier.align(Alignment.BottomCenter),
            onPlayClick = {
                uriHandler.openUrl(uiState.videoProvider)
            },
            onStarClick = {
                // TODO save favorite onclick handler
            }
        )
    }
}

@Composable
fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = uiState.name,
            color = NovixTheme.colors.title,
            style = NovixTheme.typography.title.medium,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        ) {
            GenreNames(
                uiState = uiState,
                modifier = Modifier.fillMaxWidth()
            )
            TvShowBasicDetails(
                modifier = Modifier,
                uiState = uiState
            )
        }
    }
}

@Composable
fun TvShowBasicDetails(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RatingItem(
            modifier = Modifier,
            rating = uiState.voteAverage.toLocalizedNumbers(),
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        TvShowDate(uiState)

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        Seasons(uiState)
    }
}

@Composable
fun TvShowDate(
    uiState: EpisodeDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = uiState.airDate.toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}


@Composable
fun GenreNames(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    FlowRow(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.episodeGenres.forEachIndexed { index, genre ->
                Text(
                    text = genre,
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.body,
                    modifier = Modifier.padding(end = 8.dp)
                )

                if (index != uiState.episodeGenres.lastIndex)
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(NovixTheme.colors.hint)
                    )
            }

        }

    }
}

@Composable
fun Seasons(uiState: EpisodeDetailsUiState) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_tv),
            contentDescription = "Tv icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = "${stringResource(Res.string.s)}${uiState.seasonNumber.toLocalizedNumbers()}",
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title,
        )
    }
}

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    if (uiState.overview.isNotBlank()) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(Res.string.overview),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title
            )

            ConditionalText(
                text = uiState.overview,
                expandedState = isTextCollapsed
            ) { isTextCollapsed = !isTextCollapsed }
        }
    }
}
