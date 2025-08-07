package com.london.presentation.feature.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.presentation.shared.ActorItem
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.shared.LazyPagingColumn
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading

@Composable
fun TrendingActorsScreen(
    onNavigateActor: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TrendingActorsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.NavigateToActor -> onNavigateActor(currentEffect.actorId)
            is TrendingActorsEffect.NavigateBack -> onNavigateBack()
        }
    }


    val actorsLazyItems = state.actorsFlow.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = actorsLazyItems.isLoading(),
        isError = actorsLazyItems.loadState.refresh is LoadState.Error,
        onBack = viewModel::onBack,
        onRetry = viewModel::onRetry,
        emptyLayoutMessage = R.string.no_trending_actors_in_genre,
        emptyLayoutImage = R.drawable.img_no_result,
        pagingFlow = actorsLazyItems
    ) {
        TrendingActorsContent(
            state = state,
            contract = viewModel,
        )
    }
}

@Composable
private fun TrendingActorsContent(
    state: TrendingActorsUiState = TrendingActorsUiState(),
    contract: TrendingActorsContract = defaultTrendingActorsContract(),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(color = NovixTheme.colors.surface)
    ) {
        stickyHeader {
            TopBar(
                title = stringResource(R.string.trending_people),
                onBackClick = contract::onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NovixTheme.colors.surface)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp)
            )
        }

        item {

            LazyPagingColumn(
                pagingItems = state.actorsFlow.collectAsLazyPagingItems(),
                modifier = Modifier.fillMaxSize(),
                itemContent = { actor ->
                    ActorItem(
                        actorName = actor.name,
                        characterName = null,
                        imageRes = actor.profilePictureUrl,
                        onClick = { contract.onActorClick(actor.id) }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    TrendingActorsContent()
}