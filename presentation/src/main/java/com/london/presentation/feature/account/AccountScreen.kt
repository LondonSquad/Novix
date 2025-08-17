package com.london.presentation.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.TopBar
import com.london.presentation.R
import com.london.presentation.feature.account.bottomsheet.AppearanceBottomSheet
import com.london.presentation.feature.account.bottomsheet.ContentRestrictionBottomSheet
import com.london.presentation.feature.account.bottomsheet.LanguageBottomSheet
import com.london.presentation.feature.account.bottomsheet.LogoutBottomSheet
import com.london.presentation.feature.account.components.LoggedInContent
import com.london.presentation.feature.account.components.NotLoggedInContent
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun AccountScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToMyRating: () -> Unit = {},
    onNavigateToWatchingHistory: () -> Unit = {},
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val uriHandler = LocalUriHandler.current

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is AccountEffect.MyRatingNavigation -> onNavigateToMyRating()
            is AccountEffect.WatchingHistoryNavigation -> onNavigateToWatchingHistory()
            is AccountEffect.ChangePasswordNavigation -> uriHandler.openUri(currentEffect.url)
            is AccountEffect.LoginNavigation -> onNavigateToLogin()
        }
    }

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = uiState.error != null,
        onRetry = {},
        onBack = {},
    ) {
        Content(
            uiState = uiState,
            accountContract = viewModel,
        )
    }
}

@Composable
private fun Content(
    uiState: AccountUiState,
    accountContract: AccountContract,
) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        TopBar(
            title = stringResource(R.string.my_account),
            modifier = Modifier.padding(horizontal = 16.dp)
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

    when (uiState.activeBottomSheet) {
        ActiveBottomSheet.Logout -> {
            LogoutBottomSheet(
                onBottomSheetDismiss = accountContract::onBottomSheetDismiss,
                onLogoutConfirmed = accountContract::onLogoutConfirmed,
                isLoading = uiState.isLogoutLoading
            )
        }

        ActiveBottomSheet.ContentRestriction -> {
            ContentRestrictionBottomSheet(
                currentLevel = uiState.currentContentRestriction,
                onSaveClick = accountContract::onContentRestrictionSave,
                onDismiss = accountContract::onBottomSheetDismiss
            )
        }

        ActiveBottomSheet.Appearance -> {
            AppearanceBottomSheet(
                appTheme = uiState.appTheme,
                onBottomSheetDismiss = accountContract::onBottomSheetDismiss,
                onDarkModeSelected = accountContract::onDarkModeSelected,
                onLightModeSelected = accountContract::onLightModeSelected,
                onAppearanceModeSave = accountContract::onAppearanceModeSave,
            )
        }

        ActiveBottomSheet.Language -> {
            LanguageBottomSheet(
                appLanguage = uiState.appLanguage,
                onBottomSheetDismiss = accountContract::onBottomSheetDismiss,
                onEnglishSelected = accountContract::onEnglishSelected,
                onArabicSelected = accountContract::onArabicSelected,
                onLanguageSettingsSave = accountContract::onLanguageSettingsSave,
            )
        }

        else -> {}
    }
}