package com.london.presentation.feature.list.bottomsheets

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
import com.london.presentation.feature.list.savedlist.AddSheetState
import com.london.presentation.feature.list.savedlist.ListContract
import com.london.presentation.feature.list.savedlist.ListUiState
import com.london.presentation.feature.list.savedlist.ListViewModel
import com.london.presentation.feature.list.savedlist.defaultContractList
import kotlinx.coroutines.launch

@Composable
fun AddListBottomSheet(
    modifier: Modifier = Modifier,
    addListInteractions: ListContract = hiltViewModel<ListViewModel>(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    listUiState: ListUiState,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listUiState.isSheetVisible) {
        if (listUiState.isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    if (listUiState.isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                addListInteractions.onAddListSheetDismiss()
            },
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.35f)
                    .padding(bottom = 24.dp)
            ) {
                AddListBottomSheetContent(
                    modifier = modifier,
                    addInteractions = addListInteractions,
                    addSheetState = listUiState.addListSheetState,
                    onCloseClicked = {
                        if (!listUiState.isLoading) {
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    addListInteractions.onAddListSheetDismiss()
                                }
                            }
                        }
                    },
                    onAddClicked = {
                        addListInteractions.onAddList(listUiState.addListSheetState.listName)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddListBottomSheetContent(
    modifier: Modifier = Modifier,
    addInteractions: ListContract,
    onCloseClicked: () -> Unit,
    onAddClicked: () -> Unit,
    addSheetState: AddSheetState,
) {
    val interactionSourceUserName = remember { MutableInteractionSource() }

    val isButtonEnabled = isButtonEnabled(addSheetState)

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.add_new_list),
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
                    .clickable(
                        enabled = !addSheetState.isSheetLoading,
                        onClick = onCloseClicked
                    )
                    .padding(6.dp),
                painter = painterResource(com.london.designsystem.R.drawable.cancel),
                contentDescription = "Close",
                tint = NovixTheme.colors.title
            )
        }

        OutlinedTextField(
            value = addSheetState.listName,
            interactionSource = interactionSourceUserName,
            onValueChange = addInteractions::onListNameChanged,
            label = stringResource(R.string.list_title),
            leadingIcon = painterResource(R.drawable.ic_save_list),
            isError = addSheetState.errorMessage != null,
            enabled = !addSheetState.isSheetLoading
        )

        PrimaryButton(
            text = stringResource(R.string.add),
            hasLabel = true,
            hasIcon = false,
            isLoading = addSheetState.isSheetLoading,
            icon = null,
            onClick = onAddClicked,
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private fun isButtonEnabled(addSheetState: AddSheetState): Boolean {
    val trimmedListName = addSheetState.listName.text.trim()
    val isLoading = addSheetState.isSheetLoading
    val isNameNotEmpty = trimmedListName.isNotEmpty()

    return !isLoading && isNameNotEmpty
}

@Composable
@Preview
fun PreviewAddMode() {
    AddListBottomSheetContent(
        addSheetState = AddSheetState(
            listName = TextFieldValue(""),
        ),
        onAddClicked = {},
        onCloseClicked = {},
        addInteractions = defaultContractList()
    )
}