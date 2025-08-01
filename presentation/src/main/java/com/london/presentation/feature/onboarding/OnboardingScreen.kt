package com.london.presentation.feature.onboarding

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.painter
import com.london.presentation.R
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel


@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val pagerState = rememberPagerState(
        pageCount = { uiState.pages.size },
        initialPage = uiState.currentPage
    )

    HandlePagerStateChanges(pagerState, viewModel)
    HandleEffects(effect, viewModel, pagerState, scope, onComplete)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .systemBarsPadding()
    ) {
        OnboardingContent(
            pagerState = pagerState,
            uiState = uiState,
            viewModel = viewModel,
            scope = scope
        )

        SkipButton(
            visible = !uiState.isLastPage,
            onClick = viewModel::navigateToWelcome
        )
    }
}

@Composable
private fun HandlePagerStateChanges(
    pagerState: PagerState,
    viewModel: OnboardingViewModel
) {
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage)
    }
}


@Composable
private fun HandleEffects(
    effect: OnboardingEffect?,
    viewModel: OnboardingViewModel,
    pagerState: PagerState,
    scope: CoroutineScope,
    onComplete: () -> Unit
) {
    LaunchedEffect(effect) {
        when (effect) {
            is OnboardingEffect.ScrollToPage -> {
                viewModel.scrollToPage(pagerState, effect.page, scope)
            }

            OnboardingEffect.NavigateToWelcome -> {
                viewModel.onboardingFinished()
                onComplete()
            }

            null -> {}
        }
    }
}

@Composable
private fun OnboardingContent(
    pagerState: PagerState,
    uiState: OnboardingUiState,
    viewModel: OnboardingViewModel,
    scope: CoroutineScope
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.weight(1f))
                OnboardingPageContent(uiState.pages[page])
                Spacer(Modifier.weight(1f))
            }
        }

        BottomNavigation(
            pagesCount = uiState.pages.size,
            currentPage = pagerState.currentPage,
            isFirstPage = uiState.isFirstPage,
            onPrevious = { viewModel.scrollPrevious(pagerState, scope) },
            onNext = { viewModel.scrollNext(pagerState, scope) }
        )
    }
}

@Composable
private fun BottomNavigation(
    pagesCount: Int,
    currentPage: Int,
    isFirstPage: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NovixCarousalRow(
            dotsStates = List(pagesCount) { index -> index == currentPage }
        )

        OnboardingNavigationButtons(
            isFirstPage = isFirstPage,
            onPrevious = onPrevious,
            onNext = onNext
        )
    }
}

@Composable
private fun SkipButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(visible) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onClick() },
            text = stringResource(R.string.skip),
            style = NovixTheme.typography.label.medium,
            color = NovixTheme.colors.primary
        )
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 41.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Icon(
                    painter = R.drawable.onboarding_glow.painter,
                    contentDescription = null,
                    tint = NovixTheme.colors.primary,
                    modifier = Modifier
                        .size(300.dp)
                        .blur(100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                )
            } else {
                Image(
                    modifier = Modifier.scale(2.1f),
                    painter = R.drawable.img_onboarding_glow.painter,
                    contentDescription = null
                )
            }

            Image(
                painter = page.imageRes.painter,
                contentDescription = null,
                modifier = Modifier
                    .height(244.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(page.title),
            style = NovixTheme.typography.title.large,
            color = NovixTheme.colors.title,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(page.description),
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.body,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingNavigationButtons(
    isFirstPage: Boolean,
    modifier: Modifier = Modifier,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Row(modifier = Modifier.animateContentSize()) {
            AnimatedVisibility(visible = !isFirstPage) {
                OutlineButton(
                    modifier = Modifier
                        .width(52.dp)
                        .height(48.dp),
                    text = null,
                    onClick = onPrevious,
                    isLoading = false,
                    hasIcon = true,
                    hasLabel = false,
                    icon = com.london.designsystem.R.drawable.arrow_left
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
        }
        PrimaryButton(
            modifier = Modifier
                .width(52.dp)
                .height(48.dp),
            text = null,
            onClick = onNext,
            isLoading = false,
            hasIcon = true,
            hasLabel = false,
            icon = com.london.designsystem.R.drawable.icon_arrow
        )
    }
}


@ThemePreviews
@Composable
fun OnboardingPreview() {
    OnboardingScreen(
        onComplete = {},
    )
}