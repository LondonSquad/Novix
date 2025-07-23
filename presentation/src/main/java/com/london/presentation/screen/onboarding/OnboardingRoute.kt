package com.london.presentation.screen.onboarding

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference

@Composable
fun OnboardingRoute(
    onCompleted: () -> Unit,
    onSkip: () -> Unit,
) {
    var isOnboardingCompleted by rememberBooleanPreference(
        keyName = "onboardingKey",
        initialValue = false,
        defaultValue = null,
    )
    when (isOnboardingCompleted) {
        false -> SplashScreen()
        null -> OnboardingScreen(
            onNext = {
                isOnboardingCompleted = true
                onCompleted()
            },
            onSkip = {
                isOnboardingCompleted = true
                onSkip()
            },
        )

        true -> {
            LaunchedEffect(Unit) {
                onCompleted()
            }
        }
    }
}