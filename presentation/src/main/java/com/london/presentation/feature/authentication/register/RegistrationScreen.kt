package com.london.presentation.feature.authentication.register

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.utils.Listen

@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onRegisterComplete: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is RegistrationEffect.NavigateBack -> onNavigateBack()
            is RegistrationEffect.RegistrationComplete -> onRegisterComplete()
        }
    }

    Content(
        state = uiState,
        contract = viewModel
    )
}

@Composable
private fun Content(
    state: RegistrationUiState,
    contract: RegistrationContract
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                createConfiguredWebView(context, contract)
            },
            update = { webView ->
                if (webView.url != state.registrationUrl) {
                    webView.loadUrl(state.registrationUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun createConfiguredWebView(
    context: android.content.Context,
    contract: RegistrationContract
): WebView {
    return WebView(context).apply {
        webViewClient = createWebViewClient(contract)
        configureWebViewSettings()
    }
}

private fun createWebViewClient(contract: RegistrationContract): WebViewClient {
    return object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let { contract.onPageLoaded(it) }
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            return url?.let { safeUrl ->
                contract.onUrlChanged(safeUrl)
                contract.shouldInterceptUrl(safeUrl)
            } ?: false
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureWebViewSettings() {
    settings.apply {
        setSupportZoom(true)
        javaScriptEnabled = true
        domStorageEnabled = true
        builtInZoomControls = true
        displayZoomControls = false
    }
}

