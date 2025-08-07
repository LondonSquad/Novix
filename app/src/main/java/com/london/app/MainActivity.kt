package com.london.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.london.app.navigation.NovixApp
import com.london.designsystem.theme.NovixTheme
import com.london.domain.AppPreferencesService
import com.london.domain.theme.AppTheme
import com.london.domain.theme.isDark
import com.london.presentation.shared.ContentRestrictionProvider
import com.london.presentation.shared.LocalContentRestrictionLevel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appPreferencesService: AppPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val appTheme by appPreferencesService.appTheme.collectAsState()
            val isSystemInDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

            val useDarkTheme = when (appTheme) {
                AppTheme.SYSTEM -> isSystemInDarkTheme
                else -> appTheme.name.isDark()
            }

            UpdateSystemBarsTheme(useDarkTheme)

            NovixTheme(
                isDarkMode = useDarkTheme
            ) {
                ContentRestrictionProvider(appPreferencesService) { contentRestrictionLevel ->
                    CompositionLocalProvider(
                        LocalContentRestrictionLevel provides contentRestrictionLevel
                    ) {
                        NovixApp()
                    }
                }
            }
        }
    }
}

@Composable
private fun UpdateSystemBarsTheme(useDarkTheme: Boolean) {
    val view = LocalView.current

    LaunchedEffect(useDarkTheme) {
        val window = (view.context as? ComponentActivity)?.window
        window?.let {
            WindowInsetsControllerCompat(it, view).isAppearanceLightStatusBars = !useDarkTheme
            WindowInsetsControllerCompat(it, view).isAppearanceLightNavigationBars = !useDarkTheme
        }
    }
}
