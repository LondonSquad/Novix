package com.london.presentation.feature.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.shared.LazyPagingColumn
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingActorsScreen(
    onNavigateActor: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TrendingActorsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.NavigateToActor -> onNavigateActor(currentEffect.actorId)
            is TrendingActorsEffect.NavigateBack -> onNavigateBack()
        }
    }

    TrendingActorsContent(
        state = state,
        contract = viewModel
    )
}

@Composable
private fun TrendingActorsContent(
    state: TrendingActorsUiState = TrendingActorsUiState(),
    contract: TrendingActorsContract = defaultTrendingActorsContract()
) {
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
            onBackClick = contract::onBack
        )

        val actorsLazyItems = state.actorsFlow.collectAsLazyPagingItems()


        LazyPagingColumn(
            emptyTitle = R.string.no_trending_actors_in_genre.string,
            pagingItems = actorsLazyItems,
        ) { actor ->
            ActorItem(
                modifier = Modifier.clickable { contract.onActorClick(actor.id) },
                actorName = actor.name,
                characterName = null,
                imageRes = actor.profilePicture
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    TrendingActorsContent()
}