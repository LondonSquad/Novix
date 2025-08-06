package com.london.presentation.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.london.designsystem.component.Icon
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.utils.Listen

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToWelcome: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel()
) {

    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when(currentEffect){
            SplashEffect.Home -> onNavigateToHome()
            SplashEffect.Onboarding -> onNavigateToOnboarding()
            SplashEffect.Welcome -> onNavigateToWelcome()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            tint = NovixTheme.colors.primary,
            modifier = Modifier.size(120.dp)
        )
    }
}

@ThemePreviews
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}