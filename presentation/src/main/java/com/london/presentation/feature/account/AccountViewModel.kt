package com.london.presentation.feature.account

import androidx.lifecycle.viewModelScope
import com.london.domain.AppPreferencesService
import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appPreferencesService: AppPreferencesService
) : BaseViewModel<AccountUiState, AccountEffect>(AccountUiState()),
    AccountContract {

    init {
        updateState {
            copy(isUserLoggedIn = checkIfUserIsLoggedIn())
        }
        observeContentRestrictionLevel()
    }

    private fun observeContentRestrictionLevel() {
        appPreferencesService.contentRestrictionLevel
            .onEach { level ->
                updateState { copy(currentContentRestriction = level) }
            }
            .launchIn(viewModelScope)
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

    override fun onContentRestrictionSave(level: ContentRestrictionLevel) {
        appPreferencesService.setContentRestrictionLevel(level)
        updateState {
            copy(
                showContentRestrictionBottomSheet = false,
                currentContentRestriction = level
            )
        }
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
