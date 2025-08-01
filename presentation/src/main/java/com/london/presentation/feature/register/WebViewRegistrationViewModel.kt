package com.london.presentation.feature.register

import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewRegistrationViewModel @Inject constructor() :
    BaseViewModel<WebViewRegistrationUiState, WebViewRegistrationEffect>(WebViewRegistrationUiState()),
    WebViewRegistrationContract {

    override fun onNavigateBack() {
        emitEffect(WebViewRegistrationEffect.NavigateBack)
    }

    override fun onPageLoaded(url: String?) {
        url?.let { currentUrl ->
            updateState { copy(currentUrl = currentUrl, isLoading = false) }
            if (isRegistrationCompleteUrl(currentUrl)) {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
            }
        }
    }

    override fun onUrlChanged(url: String) {
        updateState { copy(currentUrl = url) }
    }

    override fun shouldInterceptUrl(url: String): Boolean {
        if (!isUrlAllowed(url)) return true

        return when {
            isRegistrationCompleteUrl(url) -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            isAllowedRegistrationUrl(url) -> false

            isCancelUrl(url) -> {
                emitEffect(WebViewRegistrationEffect.NavigateBack)
                true
            }
            url.contains("login") && url.contains("success") -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            else -> true
        }
    }


    private fun isCancelUrl(url: String): Boolean {
        return url.contains("cancel", ignoreCase = true) ||
                url.contains("back", ignoreCase = true) ||
                url.contains("close", ignoreCase = true) ||
                url.contains("dismiss", ignoreCase = true) ||
                url.contains("exit", ignoreCase = true) ||
                url == "https://www.themoviedb.org/"
    }

    private fun isUrlAllowed(url: String): Boolean {
        return try {
            val allowedUrls = listOf(
                "https://www.themoviedb.org/signup",
                "https://themoviedb.org/signup",
                "https://www.themoviedb.org/account/signup",
                "https://themoviedb.org/account/signup",
                "https://www.themoviedb.org/",
                "https://themoviedb.org/",
                "https://www.themoviedb.org/login",
                "https://themoviedb.org/login"
            )

            val isExactMatch = allowedUrls.any { allowedUrl ->
                url.startsWith(allowedUrl)
            }

            if (isExactMatch) return true

            val completionUrls = listOf(
                "account/verify",
                "registration/success",
                "signup/complete",
                "Cancel",
                "back"
            )

            url.contains("themoviedb.org") &&
                    completionUrls.any { completionUrl -> url.contains(completionUrl) }

        } catch (e: Exception) {
            false
        }
    }

    private fun isRegistrationCompleteUrl(url: String): Boolean {
        return url.contains("account/verify") ||
                url.contains("registration/success") ||
                url.contains("signup/complete") ||
                url.contains("welcome") ||
                (url.contains("themoviedb.org") && url.contains("u/"))
    }

    private fun isAllowedRegistrationUrl(url: String): Boolean {
        val allowedUrls = listOf(
            "https://www.themoviedb.org/signup",
            "https://themoviedb.org/signup",
            "https://www.themoviedb.org/account/signup",
            "https://themoviedb.org/account/signup"
        )

        return allowedUrls.any { allowedUrl -> url.startsWith(allowedUrl) }
    }
}