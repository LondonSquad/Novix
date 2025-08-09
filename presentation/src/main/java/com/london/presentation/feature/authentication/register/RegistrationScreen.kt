package com.london.presentation.feature.authentication.register

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
fun WebViewRegistrationScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationComplete: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is RegistrationEffect.NavigateBack -> onNavigateBack()
            is RegistrationEffect.RegistrationComplete -> onRegistrationComplete()
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
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            contract.onPageLoaded(url)
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            url?.let {
                                contract.onUrlChanged(it)
                                if (contract.shouldInterceptUrl(it)) {
                                    return true
                                }
                            }
                            return false
                        }
                    }

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                    }

                    loadUrl(state.registrationUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}