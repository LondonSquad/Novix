package com.london.presentation.feature.register

import com.london.presentation.feature.base.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WebViewRegistrationViewModel :
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
        if (!isUrlAllowed(url)) { return true }

        return when {
            isRegistrationCompleteUrl(url) -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            // Add other URL patterns to intercept if needed
            url.contains("login") && url.contains("success") -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            else -> false
        }
    }

    private fun isUrlAllowed(url: String): Boolean {
        try {
            val allowedUrls = listOf(
                "https://www.themoviedb.org/signup",
                "https://themoviedb.org/signup",
                "https://www.themoviedb.org/account/signup",
                "https://themoviedb.org/account/signup"
            )

            val isExactMatch = allowedUrls.any { allowedUrl ->
                url.startsWith(allowedUrl)
            }

            if (isExactMatch) {
                return true
            }

            val completionUrls = listOf(
                "account/verify",
                "registration/success",
                "signup/complete"
            )

            return url.contains("themoviedb.org") &&
                    completionUrls.any { completionUrl -> url.contains(completionUrl) }

        } catch (e: Exception) {
            return false
        }
    }

    private fun isRegistrationCompleteUrl(url: String): Boolean {
        return url.contains("account/verify") ||
                url.contains("registration/success") ||
                url.contains("signup/complete") ||
                url.contains("welcome") ||
                (url.contains("themoviedb.org") && url.contains("u/"))
    }
}