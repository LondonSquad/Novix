package com.london.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.london.app.navigation.NovixApp
import com.london.designsystem.theme.NovixTheme
import com.london.domain.AppPreferencesService
import com.london.domain.repository.AuthRepository
import com.london.presentation.shared.ContentRestrictionProvider
import com.london.presentation.shared.LocalContentRestrictionLevel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appPreferencesService: AppPreferencesService

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            NovixTheme {
                ContentRestrictionProvider(appPreferencesService) { contentRestrictionLevel ->
                    CompositionLocalProvider(
                        LocalContentRestrictionLevel provides contentRestrictionLevel
                    ) {
                        NovixApp(appPreferencesService, authRepository)
                    }
                }
            }
        }
    }
}
