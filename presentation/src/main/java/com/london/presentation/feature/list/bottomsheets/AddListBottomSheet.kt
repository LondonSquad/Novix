package com.london.presentation.feature.list.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.feature.list.savedlist.AddSheetState
import com.london.presentation.feature.list.savedlist.ListContract
import com.london.presentation.feature.list.savedlist.defaultContractList
import kotlinx.coroutines.launch

@Composable
fun AddListBottomSheet(
    addListSheetState: AddSheetState,
    addListInteractions: ListContract,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(addListSheetState.isSheetVisible) {
        if (addListSheetState.isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    if (addListSheetState.isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                addListInteractions.setAddListSheetVisible(false)
            },
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
        ) {
            Content(
                modifier = modifier.padding(bottom = 24.dp),
                addInteractions = addListInteractions,
                addSheetState = addListSheetState,
                onCloseClicked = {
                    if (!addListSheetState.isSheetLoading) {
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                addListInteractions.setAddListSheetVisible(false)
                            }
                        }
                    }
                },
                onAddClicked = {
                    addListInteractions.onAddList(addListSheetState.listName.text)
                }
            )
        }
    }
}

@Composable
private fun Content(
    onAddClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    addInteractions: ListContract,
    addSheetState: AddSheetState,
    modifier: Modifier = Modifier,
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
                    .background(color = NovixTheme.colors.iconBackgroundLow)
                    .clickable(
                        enabled = !addSheetState.isSheetLoading,
                        onClick = onCloseClicked
                    )
                    .padding(8.dp),
                painter = com.london.designsystem.R.drawable.cancel.painter,
                contentDescription = R.string.cancel_addition_to_list.string,
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
private fun Preview() {
    Content(
        addSheetState = AddSheetState(
            listName = TextFieldValue(""),
        ),
        onAddClicked = {},
        onCloseClicked = {},
        addInteractions = defaultContractList()
    )
}
