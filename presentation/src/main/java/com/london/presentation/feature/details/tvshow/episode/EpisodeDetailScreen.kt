package com.london.presentation.feature.details.tvshow.episode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.GuestUserLoginBottomSheet
import com.london.designsystem.component.RatingBottomSheet
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.snackbar.SnackBarData
import com.london.designsystem.snackbar.SnackBarType
import com.london.designsystem.snackbar.rememberSnackBarController
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.designsystem.component.BackgroundGradient
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.item.ActorItem
import com.london.presentation.shared.item.RatingItem
import com.london.presentation.shared.section.FooterSection
import com.london.presentation.shared.text.ConditionalText
import com.london.presentation.shared.text.TextWithIcon
import com.london.presentation.utils.Listen
import com.london.presentation.utils.detailsTopBar
import com.london.presentation.utils.headerDetailsCard
import com.london.presentation.utils.isNotZeroRate
import com.london.presentation.utils.openUrl
import com.london.presentation.utils.toLocalizedNumbers
import com.london.designsystem.R as Res

@Composable
fun EpisodeDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: (tvShowId: Int, seasonNumber: Int, episodeNumber: Int) -> Unit,
    onNavigateToActorDetails: (Int) -> Unit,
    viewModel: EpisodeDetailsViewModel = hiltViewModel()
) {
    val effect by viewModel.effect.collectAsState(null)
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            EpisodeDetailsEffect.BackNavigation -> onNavigateBack()
            is EpisodeDetailsEffect.CastNavigation -> onNavigateToActorDetails(currentEffect.episodeId)
            is EpisodeDetailsEffect.LoginNavigation -> onNavigateToLogin(
                currentEffect.tvShowId,
                currentEffect.seasonNumber,
                currentEffect.episodeNumber
            )
        }
    }

    BuildScreen(
        onBack = viewModel::onBackClick,
        isLoading = uiState.isLoading,
        isError = uiState.error == ErrorState.NoInternet,
        onRetry = viewModel::onRetryClick
    ) {
        Content(
            uiState = uiState,
            contract = viewModel,
            onNavigateToCast = onNavigateToActorDetails
        )
    }
}

@Composable
private fun Content(
    uiState: EpisodeDetailsUiState,
    onNavigateToCast: (Int) -> Unit,
    contract: EpisodeDetailsContract
) {
    val uriHandler = LocalUriHandler.current
    val lazyListState = rememberLazyListState()
    val snackBarController = rememberSnackBarController()
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
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {

        BackgroundGradient(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(2f)
        )

        TopBar(
            onBackClick = contract::onBackClick,
            modifier = Modifier.detailsTopBar(backgroundAlpha),
            onClickOption1 = { /*todo on click on save*/ },
            option1Icon = Res.drawable.icon_remove,
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item { uiState.images?.let { CustomBackDropImagePager(images = it) } }

            item { HeaderDetailsCard(uiState = uiState) }

            item { OverviewSection(uiState = uiState) }

            uiState.episode?.let {
                val guestStars = uiState.episode.guestStars
                if (guestStars.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.featured_guests),
                            style = NovixTheme.typography.title.medium,
                            color = NovixTheme.colors.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 9.dp)
                        )
                    }
                }

                items(items = guestStars) { member ->
                    ActorItem(
                        actorName = member.name,
                        imageRes = member.profilePictureUrl,
                        characterName = member.characterName,
                        onClick = { onNavigateToCast(member.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(30.dp)) }
        }

        uiState.episode?.let {
            FooterSection(
                haveTrailer = uiState.episodeHaveTrailer,
                modifier = Modifier.align(Alignment.BottomCenter),
                onVideoClick = { uriHandler.openUrl(uiState.videoProvider) },
                onRateClick = contract::onRateEpisodeClick,
                isRateEnabled = uiState.isRated.not() && (uiState.episode.voteAverage.isNotZeroRate()),
            )
        }

        if (uiState.isRateBottomSheetVisible) RatingBottomSheet(
            onDismissClick = contract::onRateEpisodeClick,
            onSubmitClick = contract::onSelectRatingClick,
        )
        if (uiState.isGuestUserBottomSheetVisible) GuestUserLoginBottomSheet(
            onDismissClick = contract::onRateEpisodeClick,
            onLoginClick = {
                uiState.episode?.let {
                    contract.onLoginClick(
                        tvShowId = it.tvShowId,
                        episodeNumber = it.id,
                        seasonNumber = it.seasonNumber
                    )
                }
            },
        )
    }

    uiState.isSuccessfullyRated?.let { isRateAddedSuccessfully ->
        if (isRateAddedSuccessfully) {
            snackBarController.showSnackBar(
                SnackBarData(
                    message = Res.string.rated_successfully.string,
                    snackBarType = SnackBarType.Success,
                )
            )
        } else {
            snackBarController.showSnackBar(
                SnackBarData(
                    message = Res.string.rated_fail.string,
                    snackBarType = SnackBarType.Error,
                )
            )
        }
    }
}

@Composable
private fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    Column(
        modifier = modifier.headerDetailsCard(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        uiState.episode?.let {
            Text(
                text = uiState.episode.name,
                color = NovixTheme.colors.title,
                style = NovixTheme.typography.title.medium,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        ) {
            GenreNames(uiState = uiState)
            TvShowBasicDetails(uiState = uiState)
        }
    }
}

@Composable
private fun TvShowBasicDetails(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        uiState.episode?.let {
            if (uiState.episode.voteAverage.isNotZeroRate()) {
                RatingItem(
                    modifier = Modifier,
                    rating = uiState.episode.voteAverage.toLocalizedNumbers(),
                )
            }
        }

        TvShowDate(uiState)

        Seasons(uiState)
    }
}

@Composable
private fun TvShowDate(
    uiState: EpisodeDetailsUiState
) {
    uiState.episode?.let {
        TextWithIcon(
            icon = painterResource(Res.drawable.icon_calender),
            text = uiState.episode.airDate.toLocalizedNumbers()
        )
    }
}

@Composable
private fun GenreNames(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    FlowRow(modifier = modifier.fillMaxWidth())
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
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
private fun Seasons(uiState: EpisodeDetailsUiState) {
    uiState.episode?.let {
        TextWithIcon(
            icon = painterResource(Res.drawable.icon_tv),
            text = "${stringResource(Res.string.s)}${uiState.episode.seasonNumber.toLocalizedNumbers()}"
        )
    }
}

@Composable
private fun OverviewSection(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    uiState.episode?.let {
        Column(modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))
        {
            Text(
                text = stringResource(Res.string.overview),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title
            )
            ConditionalText(
                text = it.overview,
                expandedState = isTextCollapsed
            ) { isTextCollapsed = !isTextCollapsed }
        }
    }
}
