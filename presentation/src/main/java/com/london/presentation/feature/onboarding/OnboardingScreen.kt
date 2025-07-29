package com.london.presentation.feature.onboarding

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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val pagerState = rememberPagerState(
        pageCount = { uiState.pages.size },
        initialPage = uiState.currentPage
    )

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is OnboardingEffect.ScrollToPage -> {
                viewModel.scrollToPage(pagerState, currentEffect.page, scope)
            }

            OnboardingEffect.NavigateToWelcome -> {
                viewModel.onboardingFinished()
                onComplete()
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .systemBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageContent(uiState.pages[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NovixCarousalRow(
                    dotsStates = List(uiState.pages.size) { index -> index == pagerState.currentPage },
                )

                OnboardingNavigationButtons(
                    isFirstPage = uiState.isFirstPage,
                    onPrevious = {
                        viewModel.scrollPrevious(pagerState, scope)
                    },
                    onNext = {
                        viewModel.scrollNext(pagerState, scope)
                    }

                )
            }
        }
        AnimatedVisibility(!uiState.isLastPage) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clickable {
                        viewModel.navigateToWelcome()
                    },
                text = stringResource(R.string.skip),
                style = NovixTheme.typography.label.medium,
                color = NovixTheme.colors.primary
            )
        }
    }
}


@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_onboarding_ellipse),
                contentDescription = null,
                tint = NovixTheme.colors.primary.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(450.dp)
                    .blur(60.dp)
            )

            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .height(244.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(page.title),
            style = NovixTheme.typography.title.large,
            color = NovixTheme.colors.title,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
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