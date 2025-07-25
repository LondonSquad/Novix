package com.london.presentation.screen.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.composables.ActorsLayout
import com.london.presentation.utils.ResultOrEmpty
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptySearchLayout
import com.london.presentation.screen.home.trending.actor.TrendingActorsContract

@Composable
fun TrendingActorsScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingActorsViewModel = koinViewModel(),
    contract: TrendingActorsContract
) {
    val state = viewModel.state.collectAsState().value
    val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()
    val isLoading = actorsLazyList.loadState.refresh is androidx.paging.LoadState.Loading
    val effect = viewModel.effect.collectAsState().value
    effect?.let { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.NavigateToActor -> {
                contract.onActorClick(currentEffect.actorId)
                viewModel.resetEffect()
            }
            TrendingActorsEffect.NavigateBack -> {
                contract.onBackClick()
                viewModel.resetEffect()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.trending_people),
            onBackClick = contract::onBackClick
        )
        ResultOrEmpty(
            items = actorsLazyList.itemSnapshotList.items,
            emptyContent = {
                if (!isLoading) {
                    EmptySearchLayout(
                        text = stringResource(R.string.no_search_result_msg),
                        image = R.drawable.img_no_result,
                        modifier = modifier.padding(horizontal = 16.dp)
                    )
                }
            },
            content = {
                ActorsLayout(
                    actorsUis = actorsLazyList,
                    onActorClick = { contract.onActorClick(it.id) }
                )
            }
        )
    }
} 