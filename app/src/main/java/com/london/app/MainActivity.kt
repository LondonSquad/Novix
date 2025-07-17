package com.london.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.screen.details.tvshow.episodedetails.EpisodeDetailsScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
//                NovixApp()
                EpisodeDetailsScreen()
            }
        }
    }
}