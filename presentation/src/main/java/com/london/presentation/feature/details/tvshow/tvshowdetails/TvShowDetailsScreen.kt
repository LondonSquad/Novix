package com.london.presentation.feature.details.tvshow.tvshowdetails

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.R
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.imageharamblur.ui.ImageViewFilter
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.reviews.MediaType
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.FooterSection
import com.london.presentation.shared.RatingItem
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertDate
import com.london.presentation.utils.isNotZeroRate
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.openUrl
import com.london.presentation.utils.reverseDateFormat
import com.london.presentation.utils.toLocalizedNumbers
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvShowsDetailsScreen(
    viewModel: TvShowDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToEpisodeDetails: (tvShowId: Int, episodeNumber: Int, seasonNumber: Int) -> Unit,
    onNavigateToReviews: (tvShowId: Int, mediaType: Int) -> Unit,
    onNavigateToCast: (Int) -> Unit,
    onNavigateToGenre: (Int) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TvShowDetailsEffect.OnNavigateToEpisodeDetails -> {
                onNavigateToEpisodeDetails(
                    currentEffect.tvShowId,
                    currentEffect.episodeNumber,
                    currentEffect.seasonNumber
                )
            }

            TvShowDetailsEffect.NavigateBack -> onNavigateBack()
            is TvShowDetailsEffect.NavigateToCast -> onNavigateToCast(currentEffect.tvShowId)
            is TvShowDetailsEffect.NavigateToReviews -> onNavigateToReviews(
                currentEffect.tvShowId,
                MediaType.TvShow.mediaNum
            )

            is TvShowDetailsEffect.NavigateTotvShowsByCategoryId -> onNavigateToGenre(
                currentEffect.categoryId
            )
        }
    }

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = false,
        content = {
            TvShowsDetailScreenContent(
                uiState = uiState,
                tvShowDetailsContract = viewModel
            )
        }
    )
}

@Composable
fun TvShowsDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    tvShowDetailsContract: TvShowDetailsContract
) {
    val uriHandler = LocalUriHandler.current
    val lazyListState = rememberLazyListState()
    var footerHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

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
            onBackClick = tvShowDetailsContract::onBackClicked,
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
            option1Icon = R.drawable.icon_remove,
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = footerHeight + 16.dp)
        ) {
            // Backdrop images
            item {
                val images = uiState.tvImages
                CustomBackDropImagePager(
                    images = images?.map { it.fileUrl } ?: emptyList(),
                    isVisibleDots = (images?.size ?: 0) > 1,
                )
            }

            // Header details card
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
                    onReviewClick = {
                        tvShowDetailsContract.onReviewsClicked(
                            uiState.id,
                            MediaType.TvShow.mediaNum
                        )
                    },
                    tvShowId = uiState.id,
                    rating = uiState.voteAverage.toString(),
                    date = uiState.firstAirDate,
                    numberOfSeasons = uiState.numberOfSeasons,
                    onGenreClick = tvShowDetailsContract::OnGenreClicked
                )
            }

            // Overview title
            item {
                Text(
                    text = stringResource(R.string.overview),
                    style = NovixTheme.typography.title.medium,
                    color = NovixTheme.colors.title,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }

            // Overview content
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

            // Cast section
            item {
                CastSection(
                    modifier = Modifier.padding(top = 16.dp),
                    castMembers = uiState.cast?.cast ?: emptyList(),
                    onNavigateToCast = tvShowDetailsContract::onCastClicked
                )
            }

            // Season section header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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

                    Text(
                        text = "${
                            uiState.tvShowEpisodeCountBySeason?.episodes?.size.toString()
                                .toLocalizedNumbers()
                        } ${stringResource(R.string.episodes)}",
                        style = NovixTheme.typography.label.small,
                        color = NovixTheme.colors.hint,
                        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                    )
                }
            }

            items(
                items = uiState.tvShowEpisodes,
                key = { episode -> "${episode.showId}_${episode.seasonNumber}_${episode.episodeNumber}" }
            ) { episode ->
                EpisodeItem(
                    episode = episode,
                    onEpisodeClick = {
                        tvShowDetailsContract.onEpisodeClicked(
                            episode.showId,
                            episode.episodeNumber,
                            episode.seasonNumber,
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        FooterSection(
            haveTrailer = uiState.movieHaveTrailer,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { coordinates ->
                    footerHeight = with(density) { coordinates.size.height.toDp() }
                },
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
    uiState: TvShowDetailsUiState,
    onReviewClick: (tvShowId: Int) -> Unit,
    onGenreClick: (genreId: Int) -> Unit,
    tvShowId: Int,
    rating: String,
    date: String,
    numberOfSeasons: Int
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
                modifier = Modifier.fillMaxWidth(),
                onGenreClick = onGenreClick
            )
            TvShowBasicDetails(
                modifier = Modifier,
                rating = rating,
                date = date,
                numberOfSeasons = numberOfSeasons,
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
    uiState: TvShowDetailsUiState,
    onGenreClick: (genreId: Int) -> Unit
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
                        Modifier
                            .noRippleClickable { onGenreClick(genre.id) }
                            .padding(end = 8.dp)
                    else Modifier.noRippleClickable { onGenreClick(genre.id) }
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
    rating: String,
    date: String,
    numberOfSeasons: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (rating.isNotBlank() && rating.isNotZeroRate()) {
            RatingItem(
                rating = rating
            )
        }

        TvShowDate(date)

        Seasons(numberOfSeasons)
    }
}

@Composable
fun ViewReviewText(
    onReviewClick: (tvShowId: Int) -> Unit,
    tvShowId: Int,
) {
    Text(
        text = stringResource(R.string.view_review),
        style = NovixTheme.typography.label.medium,
        color = NovixTheme.colors.primary,
        modifier = Modifier.clickable { onReviewClick(tvShowId) }
    )
}

@Composable
fun Seasons(
    numberOfSeasons: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        Icon(
            painter = painterResource(R.drawable.icon_tv),
            contentDescription = "Tv icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(11.dp)
        )

        Text(
            text = "${numberOfSeasons.toLocalizedNumbers()} ${stringResource(R.string.seasons)}",
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body,
        )
    }
}

@Composable
fun TvShowDate(
    date: String
) {

    if (date.isEmpty() || date.isBlank()) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        Icon(
            painter = painterResource(R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(11.dp)
        )

        Text(
            text = reverseDateFormat(date),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body
        )
    }
}

@Composable
fun CastSection(
    modifier: Modifier = Modifier,
    castMembers: List<TvShowCastMemberEntity>,
    onNavigateToCast: (Int) -> Unit
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
                    modifier = Modifier
                        .widthIn(296.dp),
                    onClick = { onNavigateToCast(member.id) }
                )
            }
        }
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
            NovixChip(
                text = "${stringResource(R.string.s)}${(index + 1).toLocalizedNumbers()}",
                isSelected = index == selectedSeasonIndex,
                onClick = {
                    selectedSeasonIndex = index
                    viewModel.initializeEpisodesBySeasons(index + 1)
                }
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
    Column(modifier = modifier) {
        Text(
            text = "${
                uiState.tvShowEpisodeCountBySeason?.episodes?.size.toString().toLocalizedNumbers()
            } ${stringResource(R.string.episodes)}",
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.hint,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.tvShowEpisodes) { episode ->
                EpisodeItem(
                    episode = episode,
                    onEpisodeClick = {
                        viewModel.onEpisodeClick(
                            episode.showId,
                            episode.episodeNumber,
                            episode.seasonNumber,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    episode: TvShowEpisodeBySeasonEntity,
    onEpisodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEpisodeClick() },
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
                if (episode.voteAverage.isNotZeroRate()) {
                    RatingItem(
                        rating = episode.voteAverage.toLocalizedNumbers(),
                        color = NovixTheme.colors.hint
                    )

                    if (episode.runtime != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(NovixTheme.colors.hint)
                        )
                        EpisodeDuration(episode.runtime.toString().toLocalizedNumbers())
                    }

                    if (episode.airDate != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(NovixTheme.colors.hint)
                        )
                        Text(
                            text = convertDate(episode.airDate.toString()),
                            style = NovixTheme.typography.label.small,
                            color = NovixTheme.colors.hint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeDuration(
    durationTime: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (durationTime != null) {
            Icon(
                painter = painterResource(R.drawable.tv_show_episode_clock),
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
