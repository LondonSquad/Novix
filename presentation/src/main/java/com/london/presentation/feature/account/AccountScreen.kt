package com.london.presentation.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.london.presentation.feature.account.components.LoggedInContent
import com.london.presentation.feature.account.components.NotLoggedInContent
import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.utils.Listen

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToWatchingHistory: () -> Unit,
    onNavigateToMyRating: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToLogin: () -> Unit,
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

            is AccountEffect.NavigateToLogin -> {
                onNavigateToLogin()
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

        if (uiState.isUserLoggedIn) {
            LoggedInContent(
                uiState = uiState,
                accountContract = accountContract
            )
        } else {
            NotLoggedInContent(
                onLoginClick = accountContract::onLoginClick
            )
        }


    }
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
        ),
        AccountUiState(
            isUserLoggedIn = false
        ),
        // Logged in state
        AccountUiState(
            isUserLoggedIn = true,
            username = "@Hamsa_2025",
            currentAppearance = "Dark",
            currentLanguage = "ENG"
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
    override fun onLoginClick() {}
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