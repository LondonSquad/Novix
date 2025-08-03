package com.london.presentation.feature.list.viewlistitems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.list.viewlistitems.uistate.ItemsType
import com.london.presentation.feature.list.viewlistitems.uistate.MediaUi
import com.london.presentation.feature.list.viewlistitems.uistate.ViewListItemsUiState
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading
import kotlinx.coroutines.flow.flow

@Composable
fun ViewListItemsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: ViewListItemsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val listItems = state.listItems.collectAsLazyPagingItems()

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            ViewListItemsEffect.NavigateBack -> onNavigateBack()
            is ViewListItemsEffect.NavigationTvShowDetails -> onNavigateToTvShowDetails(
                currentEffect.id
            )

            is ViewListItemsEffect.NavigationMovieDetails -> onNavigateToMovieDetails(currentEffect.id)
        }
    }

    BuildScreen(
        onBack = viewModel::onBack,
        onRetry = listItems::refresh,
        isLoading = listItems.isLoading(),
        isError = listItems.loadState.refresh is LoadState.Error
    ) {
        Content(
            state = state,
            contract = viewModel,
            listItems = listItems
        )
    }
}

@Composable
private fun Content(
    state: ViewListItemsUiState,
    contract: ViewListItemsContract,
    listItems: LazyPagingItems<MediaUi>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            title = state.listTitle,
            onBackClick = contract::onBack,
            option2Icon = R.drawable.ic_delete,
            onClickOption1 = contract::onEditClick,
            option1Icon = R.drawable.ic_pencil_edit,
            onClickOption2 = contract::onDeleteClick,
            option2IconTint = NovixTheme.colors.redAccent,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        ItemsTypeRow(
            state = state,
            onItemsTypeClick = contract::onItemsTypeClick,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        ItemsLazyGrid(
            listItems = listItems,
            contract = contract,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ItemsTypeRow(
    state: ViewListItemsUiState,
    onItemsTypeClick: (ItemsType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(ItemsType.entries.toTypedArray()) {
            NovixChip(
                text = it.name,
                isSelected = it == state.selectedItemsType,
                onClick = { onItemsTypeClick(it) },
            )
        }
    }
}

@Composable
private fun ItemsLazyGrid(
    listItems: LazyPagingItems<MediaUi>,
    contract: ViewListItemsContract,
    modifier: Modifier = Modifier
) {
    MediaLazyPagingGrid(
        pagingFlow = listItems,
        onItemClick = {
            if (it.mediaType == MediaType.Movie) contract.onMovieClick(it.id.toInt())
            else contract.onTvShowClick(it.id.toInt())
        },
        getImageUrl = { it.posterUrl },
        getTitle = { "${it.id} media img" },
        modifier = modifier,
        onSaveClick = { contract.onRemoveMediaClick(it.id.toInt(), it.mediaType) },
        isItemSaved = { true },
    )
}

@Composable
@Preview
@ThemePreviews
private fun Preview() {
    NovixTheme {
        val listItems = flow<PagingData<MediaUi>> {
        }.collectAsLazyPagingItems()
        Content(
            state = ViewListItemsUiState(),
            contract = object : ViewListItemsContract {
                override fun onBack() {}
                override fun onRetry() {}
                override fun onEditClick() {}
                override fun onDeleteClick() {}
                override fun onMovieClick(id: Int) {}
                override fun onTvShowClick(id: Int) {}
                override fun onItemsTypeClick(itemsType: ItemsType) {}
                override fun onRemoveMediaClick(id: Int, type: MediaType) {}
            },
            listItems = listItems
        )
    }
}
