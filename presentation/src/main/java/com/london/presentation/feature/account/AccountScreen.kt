package com.london.presentation.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.TopBar
import com.london.presentation.R
import com.london.presentation.feature.account.components.LoggedInContent
import com.london.presentation.feature.account.components.NotLoggedInContent
import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToWatchingHistory: () -> Unit = {},
    onNavigateToMyRating: () -> Unit = {},
    onNavigateToChangePassword: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onLogout: () -> Unit = {}
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

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = uiState.error != null,
        onRetry = {},
        onBack = {},
    ) {
        AccountScreenContent(
            uiState = uiState,
            accountContract = viewModel
        )
    }
}

@Composable
internal fun AccountScreenContent(
    uiState: AccountUiState,
    accountContract: AccountContract
) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        TopBar(
            title = stringResource(R.string.my_account),
            modifier = Modifier.heightIn(56.dp)
                .padding(top = 12.dp)
                .padding(horizontal = 4.dp)
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