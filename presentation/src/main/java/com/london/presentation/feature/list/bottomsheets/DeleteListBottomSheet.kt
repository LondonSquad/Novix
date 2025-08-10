package com.london.presentation.feature.list.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.feature.list.viewitems.ViewListItemsContract
import kotlinx.coroutines.launch

@Composable
fun DeleteListBottomSheet(
    isSheetVisible: Boolean,
    contract: ViewListItemsContract,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = contract::onDeleteBottomSheetDismiss,
            containerColor = NovixTheme.colors.surface,
            state = sheetState
        ) {
            Content(
                modifier = modifier,
                onConfirmDelete = contract::onConfirmDelete,
                hideSheet = {
                    coroutineScope.launch { sheetState.hide() }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                contract.onDeleteBottomSheetDismiss()
                            }
                        }
                },
            )
        }
    }
}

@Composable
private fun Content(
    hideSheet: () -> Unit,
    onConfirmDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = R.string.delete_list.string,
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
            )

            Box(
                modifier = modifier
                    .background(color = NovixTheme.colors.iconBackgroundLow)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(onClick = hideSheet),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    painter = com.london.designsystem.R.drawable.cancel.painter,
                    contentDescription = R.string.cancel_list_deletion.string,
                    tint = NovixTheme.colors.title
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = if (NovixTheme.isThemeDark) R.drawable.ic_trash_dark.painter else R.drawable.ic_trash_light.painter,
                contentDescription = R.string.delete_list.string,
            )

            Text(
                text = R.string.cancel_list_deletion.string,
                style = NovixTheme.typography.body.medium,
                color = NovixTheme.colors.body,
                textAlign = TextAlign.Center
            )
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.btn_delete.string,
            hasLabel = true,
            hasIcon = false,
            isLoading = false,
            icon = null,
            onClick = {
                onConfirmDelete()
                hideSheet()
            }
        )
    }
}