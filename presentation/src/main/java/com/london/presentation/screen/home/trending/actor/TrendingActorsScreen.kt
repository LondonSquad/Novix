package com.london.presentation.screen.home.trending.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.composables.LazyPagingColumn
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingActorsScreen(
    onActorClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TrendingActorsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingActorsEffect.NavigateToActor -> onActorClick(currentEffect.actorId)
            is TrendingActorsEffect.NavigateBack -> onBackClick()
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
            onBackClick = contract::onBackClick
        )

        LazyPagingColumn(
            emptyTitle = R.string.no_trending_actors_in_genre.string,
            pagingFlow = state.actorsFlow,
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
