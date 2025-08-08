package com.london.app

import android.app.Activity
import android.content.Context
import android.graphics.Color
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.london.app.navigation.NovixApp
import com.london.data.local.preference.readLanguageCode
import com.london.designsystem.theme.NovixTheme
import com.london.domain.AppPreferencesService
import com.london.presentation.localization.LocalizationManager
import com.london.presentation.localization.wrapWithLocale
import com.london.presentation.shared.ContentRestrictionProvider
import com.london.presentation.shared.LocalContentRestrictionLevel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appPreferencesService: AppPreferencesService

    @Inject
    lateinit var localizationManager: LocalizationManager

    override fun attachBaseContext(newBase: Context) {
        val languageCode = readLanguageCode(newBase)
        val localeWrappedContext = newBase.wrapWithLocale(Locale.forLanguageTag(languageCode))
        super.attachBaseContext(localeWrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val isAppDarkMode by appPreferencesService.isAppDarkMode.collectAsState()
            val currentLocale by localizationManager.localeFlow.collectAsState(
                initial = localizationManager.getCurrentLocale()
            )
            var lastLocale by remember { mutableStateOf(currentLocale) }

            LaunchedEffect(currentLocale) {
                if (lastLocale != currentLocale) {
                    lastLocale = currentLocale
                    recreate()
                }
            }

            NovixTheme(isAppDarkMode = isAppDarkMode) {
                ApplySystemBarTheme(useDarkTheme = isAppDarkMode)

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

@Suppress("DEPRECATION")
@Composable
private fun ApplySystemBarTheme(useDarkTheme: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return

    val translucentScrimColor = "#00000000".toColorInt()

    LaunchedEffect(useDarkTheme) {
        val window = (view.context as Activity).window
        val insetsController = WindowInsetsControllerCompat(window, view)

        insetsController.isAppearanceLightStatusBars = !useDarkTheme
        insetsController.isAppearanceLightNavigationBars = !useDarkTheme

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.navigationBarColor = Color.TRANSPARENT
            window.isNavigationBarContrastEnforced = false
        } else {
            window.navigationBarColor = translucentScrimColor
        }
    }
}