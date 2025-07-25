package com.london.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.london.app.navigation.NovixApp
import com.london.designsystem.theme.NovixTheme
import com.london.domain.AppPreferencesService
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            NovixTheme {
                val appPreferencesService: AppPreferencesService by inject()
                NovixApp(appPreferencesService)
            }
        }
    }
}