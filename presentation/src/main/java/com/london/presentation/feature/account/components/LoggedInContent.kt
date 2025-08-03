package com.london.presentation.feature.account.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.account.AccountContract
import com.london.presentation.feature.account.state.AccountUiState
import com.london.designsystem.R as dsR


@Composable
fun LoggedInContent(
    uiState: AccountUiState,
    accountContract: AccountContract
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        UserProfileSection(
            username = uiState.username,
            showUserMenu = uiState.showUserMenu,
            onMenuClick = accountContract::onUserMenuClick,
            onLogoutClick = accountContract::onLogoutClick,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )

        AccountMenuItem(
            icon = painterResource(R.drawable.time_schedule),
            title = stringResource(R.string.watching_history),
            onClick = accountContract::onWatchingHistoryClick
        )

        AccountMenuDivider()

        AccountMenuItem(
            icon = painterResource(dsR.drawable.star_square),
            title = stringResource(R.string.my_rating),
            onClick = accountContract::onMyRatingClick,
        )

        AccountMenuDivider()

        AccountMenuItem(
            icon = painterResource(dsR.drawable.shield_energy),
            title = stringResource(R.string.content_restriction),
            onClick = accountContract::onContentRestrictionClick
        )

        AccountMenuDivider()

        AccountMenuItem(
            icon = painterResource(dsR.drawable.lock_key),
            title = stringResource(R.string.change_password),
            onClick = accountContract::onChangePasswordClick
        )

        AccountMenuDivider()

        AccountMenuItem(
            icon = painterResource(dsR.drawable.moon),
            title = stringResource(R.string.appearance),
            endText = uiState.currentAppearance,
            onClick = accountContract::onAppearanceClick
        )

        AccountMenuDivider()

        AccountMenuItem(
            icon = painterResource(dsR.drawable.language_circle),
            title = stringResource(R.string.language),
            endText = uiState.currentLanguage,
            onClick = accountContract::onLanguageClick
        )

        HorizontalDivider(
            color = androidx.compose.ui.graphics.Color.Transparent,
            thickness = 16.dp
        )
    }
}

@Composable
private fun AccountMenuDivider() {
    HorizontalDivider(
        color = NovixTheme.colors.stroke,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}