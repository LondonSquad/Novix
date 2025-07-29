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
        if (!isUrlAllowed(url)) {
            return true
        }

        return when {
            isRegistrationCompleteUrl(url) -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            isCancelUrl(url) -> {
                emitEffect(WebViewRegistrationEffect.NavigateBack)
                true
            }
            url.contains("themoviedb.org") && !url.contains("signup") && !url.contains("register") -> {
                emitEffect(WebViewRegistrationEffect.NavigateBack)
                true
            }
            // إذا المستخدم ضغط login بدلاً من signup
            url.contains("login") && url.contains("success") -> {
                emitEffect(WebViewRegistrationEffect.RegistrationComplete)
                true
            }
            // إذا رجع لصفحة الهوم أو أي صفحة تانية
            url == "https://www.themoviedb.org/" || url == "https://themoviedb.org/" -> {
                emitEffect(WebViewRegistrationEffect.NavigateBack)
                true
            }
            else -> false
        }
    }

    private fun isCancelUrl(url: String): Boolean {
        return url.contains("cancel") ||
                url.contains("back") ||
                url.contains("close") ||
                url.contains("dismiss") ||
                url.contains("exit") ||
                // إذا رجع من صفحة التسجيل للصفحة الرئيسية
                (url.contains("themoviedb.org") &&
                        !url.contains("signup") &&
                        !url.contains("register") &&
                        !url.contains("account"))
    }

    private fun isUrlAllowed(url: String): Boolean {
        return try {
            val allowedUrls = listOf(
                "https://www.themoviedb.org/signup",
                "https://themoviedb.org/signup",
                "https://www.themoviedb.org/account/signup",
                "https://themoviedb.org/account/signup",
                // إضافة المزيد من الـ URLs المسموحة
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
                "cancel",
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
}