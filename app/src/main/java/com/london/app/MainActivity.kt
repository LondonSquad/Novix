package com.london.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.screen.details.actor.ActorDetailsScreen
import com.london.presentation.screen.details.actor.ActorScreenContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
                ActorScreenContent(
                    onBackClick = {}
                )
            }
        }
    }
}