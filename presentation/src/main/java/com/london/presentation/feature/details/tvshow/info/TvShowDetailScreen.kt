package com.london.presentation.feature.details.tvshow.info

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.R
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.GuestUserLoginBottomSheet
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.RatingBottomSheet
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable
import com.london.domain.entity.shared.MediaType
import com.london.domain.entity.tvshow.cast.TvShowCastMember
import com.london.domain.entity.tvshow.episode.Episodes
import com.london.designsystem.component.BackgroundGradient
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.item.ActorItem
import com.london.presentation.shared.item.ImageView
import com.london.presentation.shared.item.RatingItem
import com.london.presentation.shared.section.FooterSection
import com.london.presentation.shared.text.ConditionalText
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertDate
import com.london.presentation.utils.detailsTopBar
import com.london.presentation.utils.isNotZeroRate
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.openUrl
import com.london.presentation.utils.reverseDateFormat
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun TvShowsDetailsScreen(
    onNavigateToLogin: (tvShowId: Int) -> Unit,
    onNavigateToActorDetails: (tvShowId: Int) -> Unit,
    onNavigateToTvShowCategory: (genre: TvShowGenreUi) -> Unit,
    onNavigateToReviews: (tvShowId: Int, mediaType: MediaType) -> Unit,
    onNavigateBack: () -> Unit = {},
    onNavigateToEpisodeDetails: (tvShowId: Int, episodeNumber: Int, seasonNumber: Int) -> Unit,
    viewModel: TvShowDetailsViewModel = hiltViewModel()
) {
    val effect by viewModel.effect.collectAsState(null)
    val uiState by viewModel.state.collectAsStateWithLifecycle()

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
            is TvShowDetailsEffect.NavigateToCast -> onNavigateToActorDetails(currentEffect.tvShowId)
            is TvShowDetailsEffect.NavigateToReviews -> onNavigateToReviews(
                currentEffect.tvShowId,
                MediaType.TvShow
            )

            is TvShowDetailsEffect.NavigateToTvShowsByCategoryId -> onNavigateToTvShowCategory(
                currentEffect.category
            )

            is TvShowDetailsEffect.OnLoginNavigation -> onNavigateToLogin(currentEffect.tvShowId)
        }
    }

    BuildScreen(
        onBack = viewModel::onBackClicked,
        isLoading = uiState.isLoading,
        isError = uiState.error is ErrorState.NoInternet,
        onRetry = viewModel::onRetry
    ) {
        Content(
            uiState = uiState,
            tvShowDetailsContract = viewModel
        )
    }
}

@Composable
private fun Content(
    uiState: TvShowDetailsUiState,
    tvShowDetailsContract: TvShowDetailsContract,
    modifier: Modifier = Modifier
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
            modifier = Modifier.detailsTopBar(backgroundAlpha),
            option1Icon = R.drawable.icon_remove,
        )

        BackgroundGradient(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = footerHeight + 16.dp)
        ) {
            item {
                val images = uiState.tvImages
                CustomBackDropImagePager(
                    images = images.orEmpty().map { it },
                    isVisibleDots = (images?.size ?: 0) > 1,
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
                    onReviewClick = {
                        tvShowDetailsContract.onReviewsClicked(
                            uiState.id,
                            MediaType.TvShow
                        )
                    },
                    tvShowId = uiState.id,
                    rating = uiState.voteAverage.toString(),
                    date = uiState.firstAirDate,
                    numberOfSeasons = uiState.numberOfSeasons,
                    onGenreClick = tvShowDetailsContract::onGenreClicked
                )
            }

            if (uiState.overview.isNotBlank()) {
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
            }
            if (uiState.cast?.cast?.isNotEmpty() == true) {
                item {
                    CastSection(
                        modifier = Modifier.padding(top = 16.dp),
                        castMembers = uiState.cast.cast,
                        onNavigateToCast = tvShowDetailsContract::onCastClicked
                    )
                }
            }

            if (uiState.tvShowEpisodes.isNotEmpty() && uiState.numberOfSeasons > 0) {
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

                        uiState.tvShowEpisodeCountBySeason?.episodes?.size?.let { episodeCount ->
                            if (episodeCount > 0) {
                                Text(
                                    text = "${episodeCount.toLocalizedNumbers()} ${stringResource(R.string.episodes)}",
                                    style = NovixTheme.typography.label.small,
                                    color = NovixTheme.colors.hint,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                                )
                            }
                        }
                    }
                }

                items(
                    items = uiState.tvShowEpisodes,
                    key = { episode -> "${episode.showId}_${episode.seasonNumber}_${episode.episodeNumber}" }
                ) { episode ->
                    EpisodeItem(
                        episodes = episode,
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
        }

        FooterSection(
            haveTrailer = uiState.movieHaveTrailer,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { coordinates ->
                    footerHeight = with(density) { coordinates.size.height.toDp() }
                },
            onRateClick = tvShowDetailsContract::onRateBottomSheetClick,
            isRateEnabled = uiState.isRated.not() && (uiState.voteAverage.isNotZeroRate()),
            onVideoClick = { uriHandler.openUrl(uiState.videoProvider) }

        )
        if (uiState.isRateBottomSheetVisible) RatingBottomSheet(
            onDismissClick = tvShowDetailsContract::onRateBottomSheetClick,
            onSubmitClick = tvShowDetailsContract::onSelectRatingClick,
        )
        else if (uiState.isGuestUserBottomSheetVisible) GuestUserLoginBottomSheet(
            onDismissClick = tvShowDetailsContract::onRateBottomSheetClick,
            onLoginClick = { tvShowDetailsContract.onLoginClick(uiState.id) },
        )
    }

    uiState.isSuccessfullyRated?.let { isSuccessful ->
        if (isSuccessful) {
            SnackBarAnimation(
                message = stringResource(R.string.rated_successfully),
                icon = R.drawable.ic_success,
            )
        } else {
            SnackBarAnimation(
                message = stringResource(R.string.rated_fail),
                icon = R.drawable.ic_failed
            )
        }
    }
}

@Composable
private fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    onReviewClick: (tvShowId: Int) -> Unit,
    onGenreClick: (genreUi: TvShowGenreUi) -> Unit,
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
private fun GenreNames(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    onGenreClick: (genreUi: TvShowGenreUi) -> Unit
) {
    FlowRow(
        modifier = modifier
    ) {
        uiState.tvShowGenres.forEachIndexed { index, genre ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(genre.stringResId),
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.body,
                    modifier = if (index != uiState.tvShowGenres.lastIndex)
                        Modifier
                            .noRippleClickable { onGenreClick(genre) }
                            .padding(end = 8.dp)
                    else Modifier.noRippleClickable { onGenreClick(genre) }
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
private fun TvShowBasicDetails(
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
        if (numberOfSeasons != 0) {
            Seasons(numberOfSeasons)
        }
    }
}

@Composable
private fun ViewReviewText(
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
private fun Seasons(
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
private fun TvShowDate(
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
private fun CastSection(
    modifier: Modifier = Modifier,
    castMembers: List<TvShowCastMember>,
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
private fun SeasonEpisodesDetails(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    viewModel: TvShowDetailsViewModel = hiltViewModel()
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
private fun EpisodeItem(
    episodes: Episodes,
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
        ImageView(
            model = episodes.imageUrl,
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
                text = episodes.name,
                style = NovixTheme.typography.label.large,
                color = NovixTheme.colors.title
            )

            Text(
                text = episodes.episodeType,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (episodes.voteAverage.isNotZeroRate()) {
                    RatingItem(
                        rating = episodes.voteAverage.toLocalizedNumbers(),
                        color = NovixTheme.colors.hint
                    )

                    if (episodes.runtime != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(NovixTheme.colors.hint)
                        )
                        EpisodeDuration(episodes.runtime.toString().toLocalizedNumbers())
                    }

                    if (episodes.airDate != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(NovixTheme.colors.hint)
                        )
                        Text(
                            text = convertDate(episodes.airDate.toString()),
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
private fun EpisodeDuration(
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
