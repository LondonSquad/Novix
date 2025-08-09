package com.london.presentation.feature.accountinfo.account

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
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.presentation.R
import com.london.presentation.feature.accountinfo.appearance.AppearanceBottomSheet
import com.london.presentation.feature.accountinfo.language.LanguageBottomSheet
import com.london.presentation.feature.accountinfo.logout.LogoutBottomSheet
import com.london.presentation.shared.accountComponent.ContentRestrictionBottomSheet
import com.london.presentation.shared.accountComponent.LoggedInContent
import com.london.presentation.shared.accountComponent.NotLoggedInContent
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToWatchingHistory: () -> Unit = {},
    onNavigateToMyRating: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val uriHandler = LocalUriHandler.current

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is AccountEffect.NavigateToWatchingHistory -> onNavigateToWatchingHistory()
            is AccountEffect.NavigateToMyRating -> onNavigateToMyRating()
            is AccountEffect.NavigateToChangePassword -> uriHandler.openUri(currentEffect.url)
            is AccountEffect.NavigateLogout -> onNavigateToLogin()
        }
    }

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = uiState.error != null,
        onRetry = {},
        onBack = {},
    ) {
        AccountScreenContent(
            uiState = uiState,
            accountContract = viewModel,
        )
    }
}

@Composable
internal fun AccountScreenContent(
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
            ModalBottomSheet(
                onDismissRequest = accountContract::onBottomSheetDismiss,
                state = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            ) {
                ContentRestrictionBottomSheet(
                    currentLevel = uiState.currentContentRestriction,
                    onSaveClick = accountContract::onContentRestrictionSave
                )
            }
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