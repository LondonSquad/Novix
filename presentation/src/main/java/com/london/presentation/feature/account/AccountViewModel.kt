package com.london.presentation.feature.account

import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() :
    BaseViewModel<AccountUiState, AccountEffect>(AccountUiState()),
    AccountContract {

    init {
        updateState {
            copy(isUserLoggedIn = checkIfUserIsLoggedIn())
        }
    }

    private fun checkIfUserIsLoggedIn(): Boolean {
        // todo: Implement your authentication check logic here
        // This could check shared preferences, auth repository, etc.
        return true // Replace with actual logic
    }

    override fun onWatchingHistoryClick() {
        emitEffect(AccountEffect.NavigateToWatchingHistory)
    }

    override fun onMyRatingClick() {
        emitEffect(AccountEffect.NavigateToMyRating)
    }

    override fun onContentRestrictionClick() {
        updateState { copy(showContentRestrictionBottomSheet = true) }
    }

    override fun onChangePasswordClick() {
        emitEffect(AccountEffect.NavigateToChangePassword)
    }

    override fun onAppearanceClick() {
        updateState { copy(showAppearanceBottomSheet = true) }
    }

    override fun onLanguageClick() {
        updateState { copy(showLanguageBottomSheet = true) }
    }

    override fun onUserMenuClick() {
        updateState { copy(showUserMenu = !showUserMenu) }
    }

    override fun onLogoutClick() {
        updateState {
            copy(
                showUserMenu = false,
                showLogoutBottomSheet = true
            )
        }
    }

    override fun onBottomSheetDismiss() {
        updateState {
            copy(
                showContentRestrictionBottomSheet = false,
                showAppearanceBottomSheet = false,
                showLanguageBottomSheet = false,
                showLogoutBottomSheet = false,
                showUserMenu = false
            )
        }
    }

    override fun onLoginClick() {
        emitEffect(AccountEffect.NavigateToLogin)
    }
}
