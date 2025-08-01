package com.london.presentation.feature.register

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.utils.Listen
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WebViewRegistrationScreen(
    viewModel: WebViewRegistrationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationComplete: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is WebViewRegistrationEffect.NavigateBack -> onNavigateBack()
            is WebViewRegistrationEffect.RegistrationComplete -> onRegistrationComplete()
        }
    }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                viewModel.onPageLoaded(url)
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                url?.let { 
                                    viewModel.onUrlChanged(it)
                                    if (viewModel.shouldInterceptUrl(it)) {
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
                        
                        loadUrl(uiState.registrationUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
}