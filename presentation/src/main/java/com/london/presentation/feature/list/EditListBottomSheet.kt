package com.london.presentation.feature.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import kotlinx.coroutines.launch

@Composable
fun EditListBottomSheet(
    modifier: Modifier = Modifier,
    editInteractions: EditListContract,
    sheetState: SheetState = rememberModalBottomSheetState(),
    editListState: ListUiState,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(editListState.isSheetVisible) {
        if (editListState.isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    if (editListState.isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = editInteractions::onEditListSheetDismiss,
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.35f)
                    .padding(bottom = 24.dp)
            ) {

                EditListBottomSheetContent(
                    modifier = modifier,
                    editInteractions = editInteractions,
                    listUiState = editListState,
                    onCloseClicked = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                editInteractions.onEditListSheetDismiss()
                            }
                        }
                    },
                    onSaveClicked = {
                        editInteractions.onSaveEdit(
                            editListState.listName,
                        )
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                editInteractions.onEditListSheetDismiss()
                            }
                        }
                    }
                )

            }
        }
    }
}


@Composable
private fun EditListBottomSheetContent(
    modifier: Modifier = Modifier,
    editInteractions: EditListContract,
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    listUiState: ListUiState,
) {
    val interactionSourceUserName = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.edit_list),
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
            )

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(onClick = onCloseClicked)
                    .padding(6.dp),
                painter = painterResource(com.london.designsystem.R.drawable.cancel),
                contentDescription = "Close filter",
                tint = NovixTheme.colors.title
            )
        }

        Text(
            text = stringResource(R.string.list_title),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = listUiState.listName,
            label = stringResource(R.string.username),
            interactionSource = interactionSourceUserName,
            onValueChange = editInteractions::onListNameChanged,
            leadingIcon = painterResource(R.drawable.editlisticon)
        )

        PrimaryButton(
            text = stringResource(R.string.save),
            hasLabel = true,
            hasIcon = false,
            isLoading = false,
            icon = null,
            onClick = onSaveClicked,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }

}

@Composable
@Preview
fun Preview() {
    EditListBottomSheetContent(

        onSaveClicked = {},
        listUiState = ListUiState(listName = TextFieldValue(""), isSheetVisible = true),
        onCloseClicked = {},
        editInteractions = hiltViewModel<ListViewModel>()
    )
}