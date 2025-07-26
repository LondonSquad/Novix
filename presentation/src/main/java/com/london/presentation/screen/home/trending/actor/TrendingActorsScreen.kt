package com.london.presentation.screen.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.utils.Listen
import com.london.presentation.composables.ActorsLayout
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingActorsScreen(
    onActorClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TrendingActorsViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val effect = viewModel.effect.collectAsState().value

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.NavigateToActor -> {
                onActorClick(currentEffect.actorId)
                viewModel.resetEffect()
            }
            is TrendingActorsEffect.NavigateBack -> {
                onBackClick()
                viewModel.resetEffect()
            }
        }
    }

    TrendingActorsContent(
        state = state,
        onActorClick = onActorClick,
        onBackClick = onBackClick,
        viewModel = viewModel
    )
}

@Composable
fun TrendingActorsContent(
    state: TrendingActorsUiState,
    onActorClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TrendingActorsViewModel
) {
    val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()
    val isLoading = actorsLazyList.loadState.refresh is androidx.paging.LoadState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.trending_people),
            onBackClick = onBackClick
        )
        isLoading.takeIf { it }?.let {
            CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        (!isLoading).takeIf { it }?.let {
            ActorsLayout(
                actorsUis = actorsLazyList,
                onActorClick = { onActorClick(it.id) }
            )
        }
    }
} 