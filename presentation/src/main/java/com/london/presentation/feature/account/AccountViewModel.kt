package com.london.presentation.feature.account

import androidx.lifecycle.viewModelScope
import com.london.domain.AppPreferencesService
import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import com.london.domain.usecase.GetAccountDetails
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appPreferencesService: AppPreferencesService,
    private val authenticationUseCase: AuthenticationUseCase,
    private val accountDetailsUseCase: GetAccountDetails
) : BaseViewModel<AccountUiState, AccountEffect>(AccountUiState()),
    AccountContract {

    init {
        checkUserLoginStatus()
        fetchAndSetUsername()
        observeContentRestrictionLevel()
        initializeAppLanguage()
        initializeAppTheme()
    }


    override fun onLogoutConfirmed() {
        tryToExecute(
            block = { authenticationUseCase.logout() },
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
                emitEffect(AccountEffect.NavigateLogout)
            }
        )
    }

    override fun onLogoutClick() =
        updateState {
            copy(
                showUserMenu = false,
                activeBottomSheet = ActiveBottomSheet.Logout
            )
        }

    override fun onWatchingHistoryClick() =
        emitEffect(AccountEffect.NavigateToWatchingHistory)


    override fun onMyRatingClick() =
        emitEffect(AccountEffect.NavigateToMyRating)


    override fun onContentRestrictionClick() =
        updateState { copy(activeBottomSheet = ActiveBottomSheet.ContentRestriction) }


    override fun onContentRestrictionSave(level: ContentRestrictionLevel) {
        appPreferencesService.setContentRestrictionLevel(level)
        updateState {
            copy(
                currentContentRestriction = level,
                activeBottomSheet = ActiveBottomSheet.None
            )
        }
    }

    override fun onChangePasswordClick() =
        emitEffect(AccountEffect.NavigateToChangePassword(FORGOT_PASSWORD_URL))

    override fun onAppearanceClick() =
        updateState {
            copy(activeBottomSheet = ActiveBottomSheet.Appearance)
        }

    override fun onDarkModeSelected() =
        updateState {
            copy(appTheme = AppTheme.DARK)
        }

    override fun onLightModeSelected() =
        updateState {
            copy(appTheme = AppTheme.LIGHT)
        }

    override fun onAppearanceModeSave() {
        appPreferencesService.setAppTheme(state.value.appTheme)
        onBottomSheetDismiss()
    }

    override fun showAppearanceBottomSheet() =
        updateState {
            copy(activeBottomSheet = ActiveBottomSheet.Appearance)
        }

    override fun onLanguageClick() =
        updateState {
            copy(activeBottomSheet = ActiveBottomSheet.Language)
        }

    override fun onEnglishSelected() =
        updateState {
            copy(appLanguage = AppLanguage.ENGLISH)
        }

    override fun onArabicSelected() =
        updateState {
            copy(appLanguage = AppLanguage.ARABIC)
        }

    override fun onLanguageSettingsSave() {
        appPreferencesService.setAppLanguage(state.value.appLanguage)
        updateState {
            copy(
                activeBottomSheet = ActiveBottomSheet.None,
                appLanguage = state.value.appLanguage
            )
        }
    }

    override fun onUserMenuClick() =
        updateState { copy(showUserMenu = !showUserMenu) }

    override fun onBottomSheetDismiss() =
        updateState {
            copy(
                activeBottomSheet = ActiveBottomSheet.None,
                showUserMenu = false
            )
        }

    override fun onLoginClick() =
        emitEffect(AccountEffect.NavigateLogout)

    private fun initializeAppTheme() {
        val isAppDarkMode = appPreferencesService.isAppDarkMode.value
        updateState {
            copy(appTheme = if (isAppDarkMode) AppTheme.DARK else AppTheme.LIGHT)
        }
    }

    private fun initializeAppLanguage() {
        updateState {
            copy(appLanguage = appPreferencesService.appLanguage.value)
        }
    }

    private fun observeContentRestrictionLevel() {
        appPreferencesService.contentRestrictionLevel
            .onEach { level ->
                updateState { copy(currentContentRestriction = level) }
            }
            .launchIn(viewModelScope)
    }

    private fun checkUserLoginStatus() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { isLoggedIn: Boolean ->
                updateState { copy(isUserLoggedIn = isLoggedIn) }
            },
            onError = {
                updateState { copy(isUserLoggedIn = false) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    private fun fetchAndSetUsername() {
        tryToExecute(
            block = { accountDetailsUseCase.invoke() },
            onSuccess = { accountInfo ->
                updateState {
                    copy(
                        userName = accountInfo.userName,
                        userAvatar = accountInfo.avatarPath
                    )
                }
            }
        )
    }

    companion object {
        private const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
    }
}