package com.london.presentation.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.feature.account.components.AccountMenuItem
import com.london.presentation.feature.account.components.UserProfileSection
import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.utils.Listen
import com.london.designsystem.R as dsR

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToWatchingHistory: () -> Unit,
    onNavigateToMyRating: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is AccountEffect.NavigateToWatchingHistory -> onNavigateToWatchingHistory()
            is AccountEffect.NavigateToMyRating -> onNavigateToMyRating()
            is AccountEffect.NavigateToChangePassword -> onNavigateToChangePassword()
            is AccountEffect.ShowContentRestrictionBottomSheet -> {
                viewModel.onContentRestrictionClick()
            }
            is AccountEffect.ShowAppearanceBottomSheet -> {
                viewModel.onAppearanceClick()
            }
            is AccountEffect.ShowLanguageBottomSheet -> {
                viewModel.onLanguageClick()
            }
            is AccountEffect.ShowLogoutBottomSheet -> {
                onLogout()
            }
        }
    }

    AccountScreenContent(
        uiState = uiState,
        accountContract = viewModel
    )
}

@Composable
internal fun AccountScreenContent(
    uiState: AccountUiState,
    accountContract: AccountContract
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        TopBar(
            title = stringResource(R.string.my_account),
            modifier = Modifier.padding(vertical = 12.dp),
        )

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
                onClick = accountContract:: onMyRatingClick,
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
}

@Composable
private fun AccountMenuDivider() {
    HorizontalDivider(
        color = NovixTheme.colors.stroke,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// Preview Data Provider
internal class AccountScreenPreviewProvider : PreviewParameterProvider<AccountUiState> {
    override val values = sequenceOf(
        // Default state
        AccountUiState(
            username = "@Hamsa_2025",
            currentAppearance = "Dark",
            currentLanguage = "ENG",
            showUserMenu = false
        ),
        // With dropdown menu open
        AccountUiState(
            username = "@JohnDoe_2024",
            currentAppearance = "Light",
            currentLanguage = "AR",
            showUserMenu = true
        ),
        // Different user with different settings
        AccountUiState(
            username = "@MovieLover_123",
            currentAppearance = "Light",
            currentLanguage = "ENG",
            showUserMenu = false
        )
    )
}

// Preview Mock Contract
private class PreviewAccountContract : AccountContract {
    override fun onWatchingHistoryClick() {}
    override fun onMyRatingClick() {}
    override fun onContentRestrictionClick() {}
    override fun onChangePasswordClick() {}
    override fun onAppearanceClick() {}
    override fun onLanguageClick() {}
    override fun onUserMenuClick() {}
    override fun onLogoutClick() {}
    override fun onBottomSheetDismiss() {}
}

@ThemePreviews
@Composable
fun AccountScreenPreview(
    @PreviewParameter(AccountScreenPreviewProvider::class) uiState: AccountUiState
) {
    NovixTheme {
        AccountScreenContent(
            uiState = uiState,
            accountContract = PreviewAccountContract()
        )
    }
}