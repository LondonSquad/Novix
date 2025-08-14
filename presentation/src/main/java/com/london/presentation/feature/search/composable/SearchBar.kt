package com.london.presentation.feature.search.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.recent.RecentSearch
import com.london.presentation.R
import com.london.presentation.feature.search.SearchContract
import com.london.presentation.feature.search.SearchUiState

@Composable
fun SearchBar(
    uiState: SearchUiState,
    contract: SearchContract,
    interactionSource: MutableInteractionSource,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    val focusedState = interactionSource.collectIsFocusedAsState().value

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { contract.onSearchQueryChange(it) },
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    style = NovixTheme.typography.body.small,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_search_normal),
            trailingIcon = when {
                uiState.searchQuery.text.isNotEmpty()
                        && focusedState -> {
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_remove_filled),
                            contentDescription = stringResource(R.string.clear),
                            tint = NovixTheme.colors.hint,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { contract.clearSearch() })
                    }
                }

                else -> null
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    contract.addToRecentSearches(
                        RecentSearch(
                            query = uiState.searchQuery.text,
                            timestamp = System.currentTimeMillis(),
                            id = 0
                        )
                    )
                }),
            interactionSource = interactionSource,
            modifier = Modifier.weight(1f)
        )
    }

}