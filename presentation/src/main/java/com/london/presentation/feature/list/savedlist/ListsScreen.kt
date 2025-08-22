package com.london.presentation.feature.list.savedlist

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.FloatingActionButton
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.snackbar.LocalSnackbarController
import com.london.designsystem.snackbar.SnackBarType
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.string
import com.london.domain.entity.movie.MovieList
import com.london.presentation.R
import com.london.presentation.feature.list.bottomsheets.AddListBottomSheet
import com.london.designsystem.component.BackgroundGradient
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.navBarBottomPadding
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun ListsScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToListDetails: (Int) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is ListEffect.NavigateToDetails -> onNavigateToListDetails(currentEffect.id)
            ListEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun Content(
    state: ListUiState,
    contract: ListContract,
) {
    val pagingItems = state.items.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navBarBottomPadding()
    ) {

        BuildScreen(
            onRetry = contract::onRetry,
            isLoading = state.isLoading,
            isError = state.error is ErrorState.NoInternet,
            pagingFlow = pagingItems,
            isGuest = state.isGuest,
            guestContent = { NoListFoundAsGuest(onLoginClick = contract::onLoginClick) },
            emptyContent = {
                EmptyList(
                    contract = contract,
                    addListSheetState = state.addListSheetState
                )
            },
            handlePagingLoadingAutomatically = true
        ) {

            BackgroundGradient(
                modifier = Modifier
                    .align(Alignment.TopStart)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TopBar(
                    title = stringResource(R.string.saved_list_title),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(pagingItems.itemCount) { index ->
                        val item = pagingItems[index]
                        item?.let {
                            SavedListItemRow(
                                itemUi = item,
                                onCountClick = contract::onListClick
                            )
                        }
                    }
                }
            }

            ListFAB(contract::onFabClick)

            AddListBottomSheet(
                addListInteractions = contract,
                addListSheetState = state.addListSheetState
            )

            val snackBarController = LocalSnackbarController.current

            if (state.error != null) {
                snackBarController.showMessage(
                    message = R.string.list_added_fail.string,
                    snackBarType = SnackBarType.Error,
                    onComplete = contract::resetSnackBarErrorState,
                    icon = null,
                )
            }

            if (state.isSnackBarSuccessVisible) {
                snackBarController.showMessage(
                    message = R.string.list_added_success.string,
                    snackBarType = SnackBarType.Success,
                    onComplete = contract::resetSnackBarSuccessState,
                    icon = com.london.designsystem.R.drawable.ic_success,
                )
            }
        }
    }
}

@Composable
fun BoxScope.ListFAB(
    onFabClick: () -> Unit
) {
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

@Composable
private fun SavedListItemRow(
    itemUi: MovieList,
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
            .zIndex(2f)
    ) {
        Text(
            text = itemUi.name,
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
    itemUi: MovieList,
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
            text = itemUi.moviesCount.toLocalizedNumbers(),
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
private fun EmptyList(
    contract: ListContract,
    addListSheetState: AddSheetState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = R.string.saved_list_title.string,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )

        EmptyLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            text = stringResource(R.string.no_saved_list),
            imageContent = {
                BlurredImage(imageId = R.drawable.ic_no_saved_list_yet)
            },
        )

        ListFAB(contract::onFabClick)
    }

    AddListBottomSheet(
        addListInteractions = contract,
        addListSheetState = addListSheetState
    )
}

@Composable
private fun NoListFoundAsGuest(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            title = R.string.saved_list_title.string,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )

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

@Composable
private fun BlurredImage(@DrawableRes imageId: Int) {
    Box(
        modifier = Modifier
            .size(138.dp),
        contentAlignment = Alignment.Center
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Icon(
                painter = R.drawable.ellipse_blur_filled.painter,
                contentDescription = null,
                tint = NovixTheme.colors.redAccent,
                modifier = Modifier
                    .size(23.dp)
                    .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(NovixTheme.colors.redAccent)
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
            painter = imageId.painter,
            contentDescription = "List Image",
            modifier = Modifier
                .size(128.dp),
        )
    }
}
