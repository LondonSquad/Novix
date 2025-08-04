package com.london.presentation.feature.list.savedlist

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.FloatingActionButton
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.painter
import com.london.presentation.R
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun ListScreen(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is ListEffect.NavigateToDetails -> onNavigateToDetails(currentEffect.id)
            ListEffect.ShowAddListSheet -> TODO()
            is ListEffect.ShowEditListSheet -> TODO()
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun ScreenScaffold(
    titleRes: Int,
    showFab: Boolean = true,
    onFabClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            title = stringResource(titleRes),
            modifier = Modifier
                .statusBarsPadding()
                .heightIn(56.dp)
                .padding(top = 12.dp)
                .padding(horizontal = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            content()

            if (showFab && onFabClick != null) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp),
                    onClick = onFabClick,
                    isLoadingIcon = false,
                    isDisabledIcon = false,
                    isDefaultIcon = true
                )
            }
        }
    }
}


@Composable
private fun Content(
    state: ListUiState,
    contract: ListContract,
) {
    val pagingItems = state.items.collectAsLazyPagingItems()
    BuildScreen(
        onRetry = contract::onRetry,
        isLoading = state.isLoading,
        isError = state.error is ErrorState.NoInternet,
        pagingFlow = pagingItems,
        isGuest = state.isGuest,
        guestContent = {
            NoListFoundAsGuest(
                onLoginClick = contract::onLoginClick
            )
        },
        emptyContent = {
            EmptyList(contract = contract)
        }
    ) {
        ScreenScaffold(
            titleRes = R.string.saved_list_title,
            onFabClick = contract::onFabClick
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pagingItems.itemSnapshotList.items) { item ->
                    SavedListItemRow(
                        itemUi = item,
                        onCountClick = contract::onListClick
                    )
                }
            }
        }
    }

}

@Composable
private fun SavedListItemRow(
    itemUi: ListItemUi,
    onCountClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCountClick(itemUi.id) }
            .background(NovixTheme.colors.surface)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Text(
            text = itemUi.title,
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )
        ItemCount(
            itemUi = itemUi,
        )
    }
}

@Composable
private fun ItemCount(
    itemUi: ListItemUi,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = NovixTheme.colors.primaryVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = itemUi.count.toString(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.primary,
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = stringResource(com.london.designsystem.R.string.arrow),
            tint = NovixTheme.colors.primary
        )
    }
}

@Composable
private fun EmptyList(contract: ListContract) {
    ScreenScaffold(
        titleRes = R.string.saved_list_title,
        onFabClick = contract::onFabClick,
        showFab = true,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            EmptyLayout(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                text = stringResource(R.string.no_saved_list),
                imageContent = {
                    BlurredImage(imageId = R.drawable.ic_no_saved_list_yet)
                },
            )
        }
    }
}

@Composable
private fun NoListFoundAsGuest(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        titleRes = R.string.saved_list_title,
        showFab = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            EmptyLayout(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                text = stringResource(R.string.login_to_create_list),
                imageContent = {
                    BlurredImage(imageId = R.drawable.ic_no_saved_list_as_guest)
                },
                additionalContent = {
                    OutlineButton(
                        text = stringResource(R.string.login),
                        hasLabel = true,
                        icon = null,
                        hasIcon = false,
                        isLoading = false,
                        onClick = onLoginClick,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun BlurredImage(@DrawableRes imageId: Int) {
    Box(
        modifier = Modifier
            .size(128.dp),
        contentAlignment = Alignment.Center
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Icon(
                painter = R.drawable.ellipse_blur_filled.painter,
                contentDescription = null,
                tint = NovixTheme.colors.primary,
                modifier = Modifier
                    .size(23.dp)
                    .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .align(Alignment.BottomCenter)
            )
        } else {
            Image(
                modifier = Modifier
                    .scale(2.1f)
                    .size(23.dp)
                    .align(Alignment.BottomCenter),
                painter = R.drawable.ellipse_pre_blurred.painter,
                contentDescription = null
            )
        }
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "List Image",
            modifier = Modifier
                .size(128.dp),
        )
    }
}

@Composable
@Preview
@ThemePreviews
private fun Preview() {
    NovixTheme {
        val state = ListUiState()
        Content(
            state = state,
            contract = defaultContractList()
        )
    }
}
