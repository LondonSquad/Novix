package com.london.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            title = stringResource(R.string.search),
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = TextFieldValue(uiState.searchQuery),
                onValueChange = { viewModel.onSearchQueryChange(it.text) },
                placeholder = {
                    Text(
                        stringResource(R.string.search_placeholder),
                        style = NovixTheme.typography.body.small,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                },
                leadingIcon = painterResource(id = R.drawable.icon_search_normal),
                trailingIcon = if (uiState.isSearching) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = NovixTheme.colors.primary
                        )
                    }
                } else if (uiState.searchQuery.isNotEmpty()) {
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_remove_filled),
                            contentDescription = stringResource(R.string.clear),
                            tint = NovixTheme.colors.hint,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { viewModel.clearSearch() }
                        )
                    }
                } else null,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onSearchSubmit()
                        keyboardController?.hide()
                    }
                ),
                interactionSource = interactionSource,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            PrimaryButton(
                text = "",
                onClick = {},
                isLoading = false,
                isDisabled = false,
                hasIcon = true,
                icon = R.drawable.icon_filter,
                hasLabel = false,
                modifier = Modifier.width(52.dp)
            )
        }

        if (uiState.searchQuery.isNotEmpty() && uiState.searchResults.isNotEmpty()) {
            SectionHeader(
                text = "Search Results",
                hasGetAll = false,
                hasIcon = false,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(uiState.searchResults) { result ->
                    SearchResultItem(
                        result = result,
                        onItemClick = { /* Handle item click */ }
                    )
                }
            }
        } else if (uiState.searchQuery.isEmpty()) {
            if (uiState.recentViewed.isNotEmpty()) {
                SectionHeader(
                    text = stringResource(R.string.recent_viewed),
                    hasGetAll = true,
                    hasIcon = false,
                    getAllText = stringResource(R.string.clear_all),
                    onClick = { viewModel.clearRecentViewed() },
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(uiState.recentViewed) { imageUrl ->
                        HomeCard(
                            imageUrl = imageUrl,
                            isSaved = false,
                            onSaveClick = {}
                        )
                    }
                }
            }

            if (uiState.recentSearches.isNotEmpty()) {
                SectionHeader(
                    text = stringResource(R.string.recent_search),
                    hasGetAll = true,
                    hasIcon = false,
                    getAllText = stringResource(R.string.clear_all),
                    onClick = { viewModel.clearRecentSearches() },
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(uiState.recentSearches) { search ->
                        RecentSearchItem(
                            search = search,
                            onSearchClick = { viewModel.onRecentSearchClick(search) },
                            onRemoveClick = { viewModel.removeRecentSearch(search) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    result: SearchResult,
    onItemClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(result) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = result.title,
                style = NovixTheme.typography.body.medium,
                color = NovixTheme.colors.title
            )
            if (result.year != null || result.rating != null) {
                Text(
                    text = buildString {
                        result.year?.let { append(it) }
                        if (result.year != null && result.rating != null) append(" • ")
                        result.rating?.let { append("★ $it") }
                    },
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.hint
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    search: String,
    onSearchClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_clock),
            contentDescription = stringResource(R.string.clock),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp, bottom = 2.dp, end = 8.dp)
        )
        Text(
            text = search,
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_remove_filled),
            contentDescription = stringResource(R.string.clear),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .clickable { onRemoveClick() }
                .padding(vertical = 4.dp)
        )
    }
}


@ThemePreviews
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
