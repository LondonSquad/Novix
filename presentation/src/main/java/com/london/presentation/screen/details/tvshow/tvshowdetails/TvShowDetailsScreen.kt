package com.london.presentation.screen.details.tvshow.tvshowdetails

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.R
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.presentation.composables.ConditionalText
import com.london.presentation.composables.DetailsScreenTopBar
import com.london.presentation.composables.FooterSection
import com.london.presentation.screen.reviews.MediaType
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertDate
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.toLocalizedNumbers
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvShowsDetailsScreen(
    viewModel: TvShowDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToEpisodeDetails: (tvShowId: Int, episodeNumber: Int, seasonNumber: Int) -> Unit,
    onNavigateToReviews: (tvShowId: Int, mediaType: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val effect by viewModel.effect.collectAsState(null)

    TvShowsDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewReviewsClick = onNavigateToReviews
    )

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TvShowDetailsEffect.OnNavigateToEpisodeDetails -> {
                onNavigateToEpisodeDetails(
                    currentEffect.tvShowId,
                    currentEffect.episodeNumber,
                    currentEffect.seasonNumber
                )
            }
        }
    }
}

@Composable
fun TvShowsDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    onBackClick: () -> Unit,
    onViewReviewsClick: (tvShowId: Int, mediaType: Int) -> Unit
) {
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
        label = "background_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {


        DetailsScreenTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .align(Alignment.TopCenter),
            isSaved = uiState.isSaved,
            backgroundAlpha = backgroundAlpha,
            onBackClick = onBackClick,
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                val images = uiState.tvImages
                CustomBackDropImagePager(
                    images = images ?: emptyList()
                )
            }

            item {
                HeaderDetailsCard(
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offsetLayout()
                        .padding(start = 16.dp, end = 16.dp)
                        .heightIn(min = 158.dp)
                        .border(
                            width = 1.dp,
                            color = NovixTheme.colors.stroke,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(NovixTheme.colors.surface),
                    onReviewClick = { onViewReviewsClick(uiState.id, MediaType.TvShow.mediaNum) },
                    tvShowId = uiState.id
                )
            }

            item {
                Text(
                    text = stringResource(R.string.overview),
                    style = NovixTheme.typography.title.medium,
                    color = NovixTheme.colors.title,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }

            item {
                var isExpanded by remember { mutableStateOf(false) }
                ConditionalText(
                    text = uiState.overview,
                    expandedState = isExpanded,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    isExpanded = !isExpanded
                }
            }

            item {
                CastSection(
                    modifier = Modifier.padding(top = 16.dp),
                    castMembers = uiState.cast?.cast ?: emptyList()
                )
            }

            item {
                SeasonDetailsSection(
                    uiState = uiState,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }




        FooterSection(
            haveTrailer = uiState.haveTrailer,
            modifier = Modifier.align(Alignment.BottomCenter),
            onPlayClick = {
                // TODO play trailer onclick handler
            },
            onStarClick = {
                // TODO save favorite onclick handler
            }
        )
    }
}

@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<ImageItemEntity>
) {
    if (images.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(252.dp)
                .background(NovixTheme.colors.surface)
        ) {
            NovixCarousalRow(
                dotsStates = listOf(false),
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = NovixTheme.colors.iconBackgroundLow, shape = RoundedCornerShape(8.dp))
                    .border(width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(252.dp)
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
        ) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { images.size }
            )

            LaunchedEffect(Unit) {
                if (images.size > 1) {
                    while (true) {
                        delay(4000)
                        val nextPage = (pagerState.currentPage + 1) % images.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }

            HorizontalPager(
                modifier = Modifier.align(Alignment.Center),
                state = pagerState,
            ) { pageIndex ->
                ImageViewFilter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp),
                    contentScale = ContentScale.FillBounds,
                    model = images[pageIndex].fileUrl,
                    contentDescription = "TV Show Image ${pageIndex + 1}",
                    errorContent = { ErrorImage() },
                    loadingContent = { CircularLoading(modifier = Modifier) },
                    moderatedContent = { UnSuitableEye() }
                )
            }

            NovixCarousalRow(
                dotsStates = List(images.size) { index -> index == pagerState.currentPage },
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = NovixTheme.colors.iconBackgroundLow, shape = RoundedCornerShape(8.dp))
                    .border(width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    onReviewClick: (tvShowId: Int) -> Unit,
    tvShowId: Int
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

            ViewReviewText(
                onReviewClick = { onReviewClick(tvShowId) },
                tvShowId = tvShowId
            )
        }
    }
}

@Composable
fun GenreNames(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    FlowRow(
        modifier = modifier
    ) {
        uiState.tvShowGenres.forEachIndexed { index, genre ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = genre.name,
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.body,
                    modifier = if (index != uiState.tvShowGenres.lastIndex)
                        Modifier.padding(end = 8.dp) else Modifier
                )

                if (index != uiState.tvShowGenres.lastIndex) {
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
}


@Composable
fun TvShowBasicDetails(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TvShowRating(uiState = uiState)

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
fun ViewReviewText(
    onReviewClick: (tvShowId: Int) -> Unit,
    tvShowId: Int,
) {
    Text(
        text = stringResource(R.string.view_review),
        style = NovixTheme.typography.title.medium,
        color = NovixTheme.colors.primary,
        modifier = Modifier.clickable { onReviewClick(tvShowId) }
    )
}

@Composable
fun Seasons(uiState: TvShowDetailsUiState) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_tv),
            contentDescription = "Tv icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = "${uiState.numberOfSeasons.toLocalizedNumbers()} ${stringResource(R.string.seasons)}",
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title,
        )
    }
}

@Composable
fun TvShowDate(
    uiState: TvShowDetailsUiState
) {

    if (uiState.firstAirDate.isEmpty() || uiState.firstAirDate.isBlank()) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = uiState.firstAirDate.toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}

@Composable
fun TvShowRating(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RatingBar(
            modifier = modifier.size(12.dp),
            rating = uiState.voteAverage.toInt(),
            onRatingChanged = {},
            maxRating = 1
        )

        Text(
            text = uiState.voteAverage.toString().toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}

@Composable
fun CastSection(
    modifier: Modifier = Modifier,
    castMembers: List<TvShowCastMemberEntity>,
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.cast),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(start = 16.dp, bottom = 9.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(castMembers) { member ->
                ActorItem(
                    actorName = member.name,
                    characterName = "${member.roles[0].character} - ${member.roles[0].episodeCount}",
                    imageRes = member.profileUrl.orEmpty(),
                    modifier = Modifier.widthIn(296.dp)
                )
            }
        }
    }
}

@Composable
fun SeasonDetailsSection(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.season),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
        )

        SeasonEpisodesDetails(
            modifier = Modifier.fillMaxWidth(),
            uiState = uiState
        )

        EpisodeRow(
            uiState = uiState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SeasonEpisodesDetails(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    viewModel: TvShowDetailsViewModel = koinViewModel()
) {
    var selectedSeasonIndex by rememberSaveable { mutableIntStateOf(0) }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(uiState.numberOfSeasons) { index ->
            val isSelected = index == selectedSeasonIndex

            Text(
                text = "${stringResource(R.string.s)}${(index + 1).toLocalizedNumbers()}",
                style = NovixTheme.typography.label.medium,
                color = if (isSelected) NovixTheme.colors.onPrimary else NovixTheme.colors.body,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        selectedSeasonIndex = index
                        viewModel.initializeEpisodesBySeasons(index + 1)
                    }
                    .then(
                        if (isSelected)
                            Modifier
                                .background(
                                    color = NovixTheme.colors.secondary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        else
                            Modifier
                    )
            )
        }
    }
}

@Composable
fun EpisodeRow(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    viewModel: TvShowDetailsViewModel = koinViewModel()
) {
    Text(
        text = "${
            uiState.tvShowEpisodeCountBySeason?.episodes?.size.toString().toLocalizedNumbers()
        } ${stringResource(R.string.episodes)}",
        style = NovixTheme.typography.label.small,
        color = NovixTheme.colors.hint,
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
    )

    uiState.tvShowEpisodes.forEach { episode ->
        Row(
            modifier = modifier
                .clickable {
                    viewModel.onEpisodeClick(
                        episode.showId,
                        episode.episodeNumber,
                        episode.seasonNumber,
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ImageViewFilter(
                model = episode.stillUrl,
                contentDescription = stringResource(R.string.s),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(78.dp)
                    .weight(0.35f),
                loadingContent = { CircularLoading() },
                errorContent = { ErrorImage() },
                moderatedContent = { UnSuitableEye() }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(0.65f)
            ) {

                Text(
                    text = episode.name,
                    style = NovixTheme.typography.label.large,
                    color = NovixTheme.colors.title
                )

                Text(
                    text = episode.episodeType,
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.hint
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    EpisodeRating(rating = episode.voteAverage.toString())

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(NovixTheme.colors.hint)
                    )

                    if (episode.runtime != null) {
                        EpisodeDate(episode.runtime.toString().toLocalizedNumbers())

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(NovixTheme.colors.hint)
                        )
                    }

                    if (episode.airDate != null)
                        Text(
                            text = convertDate(episode.airDate.toString()),
                            style = NovixTheme.typography.label.small,
                            color = NovixTheme.colors.hint
                        )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun EpisodeRating(
    modifier: Modifier = Modifier,
    rating: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RatingBar(
            modifier = modifier.size(12.dp),
            rating = 1,
            onRatingChanged = {},
            maxRating = 1
        )

        Text(
            text = rating,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.hint
        )
    }
}

@Composable
fun EpisodeDate(
    durationTime: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (durationTime != null) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.tv_show_episode_clock),
                contentDescription = "Calender icon",
                tint = NovixTheme.colors.hint,
                modifier = Modifier.size(12.dp)
            )

            Text(
                text = "$durationTime${stringResource(R.string.m)}",
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint
            )
        }
    }
}