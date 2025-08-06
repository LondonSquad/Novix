package com.london.presentation.feature.account

import com.london.domain.AppPreferencesService
import com.london.domain.theme.AppTheme
import com.london.domain.usecase.login.LogoutUseCase
import com.london.presentation.feature.account.state.AccountUiState
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appPreferencesService: AppPreferencesService,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<AccountUiState, AccountEffect>(AccountUiState()),
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

    //region Appearance Bottom Sheet
    override fun onAppearanceClick() {
        updateState {
            copy(isAppearanceBottomSheetVisible = true)
        }
    }

    override fun onDarkModeSelected() {
        updateState {
            copy(appTheme = AppTheme.DARK)
        }
    }

    override fun onLightModeSelected() {
        updateState {
            copy(appTheme = AppTheme.LIGHT)
        }
    }

    override fun onAppearanceModeSave() {
        appPreferencesService.setAppTheme(state.value.appTheme)
        updateState {
            copy(isAppearanceBottomSheetVisible = false)
        }
    }

    override fun showAppearanceBottomSheet() {
        updateState {
            copy(isAppearanceBottomSheetVisible = true)
        }
    }
    //endregion

    //region Logout Bottom Sheet
    override fun onLogoutConfirmed() {
        tryToExecute(
            block = { logoutUseCase.invoke() },
            onStart = {
                updateState { copy(isLogoutLoading = true) }
            },
            onSuccess = { isSuccess: Boolean ->
                if (isSuccess) {
                    updateState { copy(isLogoutLoading = false) }
                } else {
                    updateState {
                        copy(error = ErrorState.RequestFailed("Logout failed"))
                    }
                }
            },
            onCompleted = {
                emitEffect(AccountEffect.NavigateToLogin)
            }
        )
    }

    override fun onLogoutClick() {
        updateState {
            copy(
                showUserMenu = false,
                isLogoutBottomSheetVisible = true
            )
        }
    }
    //endregion

    override fun onLanguageClick() {
        updateState { copy(isLanguageBottomSheetVisible = true) }
    }

    override fun onUserMenuClick() {
        updateState { copy(showUserMenu = !showUserMenu) }
    }

    override fun onBottomSheetDismiss() {
        updateState {
            copy(
                showContentRestrictionBottomSheet = false,
                isAppearanceBottomSheetVisible = false,
                isLanguageBottomSheetVisible = false,
                isLogoutBottomSheetVisible = false,
                showUserMenu = false
            )
        }
    }

    override fun onLoginClick() {
        emitEffect(AccountEffect.NavigateToLogin)
    }
}