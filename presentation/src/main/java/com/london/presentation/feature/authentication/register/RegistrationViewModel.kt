package com.london.presentation.feature.authentication.register

import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() :
    BaseViewModel<RegistrationUiState, RegistrationEffect>(RegistrationUiState()),
    RegistrationContract {

    override fun onNavigateBack() {
        emitEffect(RegistrationEffect.NavigateBack)
    }

    override fun onPageLoaded(url: String?) {
        url?.let { currentUrl ->
            updateState { copy(currentUrl = currentUrl, isLoading = false) }
            if (isRegistrationCompleteUrl(currentUrl)) {
                emitEffect(RegistrationEffect.RegistrationComplete)
            }
        }
    }

    override fun onUrlChanged(url: String) {
        updateState { copy(currentUrl = url) }
    }

    override fun shouldInterceptUrl(url: String): Boolean {
        if (!isUrlAllowed(url)) {
            return true
        }

        return when {
            isRegistrationCompleteUrl(url) || isSuccessfulLogin(url) -> {
                emitEffect(RegistrationEffect.RegistrationComplete)
                true
            }

            isCancelUrl(url) -> {
                emitEffect(RegistrationEffect.NavigateBack)
                true
            }

            isAllowedRegistrationUrl(url) -> false

            // Block all other URLs by default
            else -> true
        }
    }

    private fun isCancelUrl(url: String): Boolean {
        return url.contains(CANCEL_LOWERCASE, ignoreCase = true) ||
            url.contains(BACK, ignoreCase = true) ||
            url.contains(CLOSE, ignoreCase = true) ||
            url.contains(DISMISS, ignoreCase = true) ||
            url.contains(EXIT, ignoreCase = true) ||
            url == URL
    }

    private fun isUrlAllowed(url: String): Boolean {
        return try {
            val allowedUrls = listOf(
                URL,
                SITE,
                SIGNUP,
                WWW_SIGNUP,
                ACCOUNT_SIGNUP,
                LOGIN_WITH_SITE,
                WWW_ACCOUNT_SIGNUP,
                WWW_LOGIN_WITH_SITE
            )

            val isExactMatch = allowedUrls.any { allowedUrl ->
                url.startsWith(allowedUrl)
            }

            if (isExactMatch) return true

            val completionUrls = listOf(
                ACCOUNT_VERIFY,
                REGISTRATION_SUCCESS,
                SIGNUP_COMPLETE,
                CANCEL_CAPITALIZED,
                BACK
            )

            url.contains(BASE_URL) &&
                completionUrls.any { completionUrl -> url.contains(completionUrl) }

        } catch (e: Exception) {
            false
        }
    }

    private fun isRegistrationCompleteUrl(url: String): Boolean {
        return url.contains(ACCOUNT_VERIFY) ||
            url.contains(REGISTRATION_SUCCESS) ||
            url.contains(SIGNUP_COMPLETE) ||
            url.contains(WELCOME) ||
            (url.contains(BASE_URL) && url.contains("u/"))
    }

    private fun isAllowedRegistrationUrl(url: String): Boolean {
        val allowedUrls = listOf(
            WWW_SIGNUP,
            SIGNUP,
            WWW_ACCOUNT_SIGNUP,
            ACCOUNT_SIGNUP
        )

        return allowedUrls.any { allowedUrl -> url.startsWith(allowedUrl) }
    }

    private fun isSuccessfulLogin(url: String): Boolean {
        return url.contains(LOGIN) && url.contains(SUCCESS)
    }

    private companion object Registration {
        const val LOGIN = "login"
        const val SUCCESS = "success"
        const val CANCEL_LOWERCASE = "cancel"
        const val CANCEL_CAPITALIZED = "Cancel"
        const val BACK = "back"
        const val CLOSE = "close"
        const val DISMISS = "dismiss"
        const val EXIT = "exit"
        const val WELCOME = "welcome"
        const val URL = "https://www.themoviedb.org/"
        const val WWW_SIGNUP = "https://www.themoviedb.org/signup"
        const val SIGNUP = "https://themoviedb.org/signup"
        const val WWW_ACCOUNT_SIGNUP = "https://www.themoviedb.org/account/signup"
        const val ACCOUNT_SIGNUP = "https://themoviedb.org/account/signup"
        const val SITE = "https://themoviedb.org/"
        const val WWW_LOGIN_WITH_SITE = "https://www.themoviedb.org/login"
        const val LOGIN_WITH_SITE = "https://themoviedb.org/login"
        const val ACCOUNT_VERIFY = "account/verify"
        const val REGISTRATION_SUCCESS = "registration/success"
        const val SIGNUP_COMPLETE = "signup/complete"
        const val BASE_URL = "themoviedb.org"
    }
}
