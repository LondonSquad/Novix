package com.london.presentation.feature.login.registrasion

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
            
            // Check if registration is complete based on URL patterns
            if (isRegistrationCompleteUrl(currentUrl)) {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
            }
        }
    }

    override fun onUrlChanged(url: String) {
        updateState { copy(currentUrl = url) }
    }

    override fun shouldInterceptUrl(url: String): Boolean {
        // Intercept URLs that indicate registration completion
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

    private fun isRegistrationCompleteUrl(url: String): Boolean {
        return url.contains("account/verify") || 
               url.contains("registration/success") ||
               url.contains("signup/complete") ||
               url.contains("welcome") ||
               (url.contains("themoviedb.org") && url.contains("u/"))
    }
}