package com.london.presentation.feature.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.container.ActorLazyVerticalColumn
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading

@Composable
fun TrendingActorsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToActorDetails: (Int) -> Unit,
    viewModel: TrendingActorsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.ActorDetailsNavigation -> onNavigateToActorDetails(
                currentEffect.actorId
            )

            is TrendingActorsEffect.BackNavigation -> onNavigateBack()
        }
    }

    Content(
        state = state,
        contract = viewModel
    )
}

@Composable
private fun Content(
    state: TrendingActorsUiState,
    contract: TrendingActorsContract,
) {
    val actorsLazyItems = state.actorsFlow.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = actorsLazyItems.isLoading(),
        isError = actorsLazyItems.loadState.refresh is LoadState.Error,
        onBack = contract::onBackClick,
        onRetry = contract::onRetryClick,
        emptyLayoutMessage = R.string.no_trending_actors_in_genre,
        emptyLayoutImage = R.drawable.img_no_result,
        pagingFlow = actorsLazyItems
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NovixTheme.colors.surface)
        ) {
            TopBar(
                title = stringResource(com.london.designsystem.R.string.tv_shows),
                onBackClick = contract::onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NovixTheme.colors.surface)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            ActorLazyVerticalColumn(
                items = actorsLazyItems,
                onActorClick = { contract.onActorClick(it.id) }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    Content(
        state = TrendingActorsUiState(),
        contract = object : TrendingActorsContract {
            override fun onActorClick(id: Int) {}
            override fun onBackClick() {}
            override fun onRetryClick() {}
        }
    )
}
