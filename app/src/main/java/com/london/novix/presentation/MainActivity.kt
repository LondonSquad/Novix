package com.london.novix.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FakeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FakeScreen(modifier: Modifier = Modifier) {
    Scaffold(containerColor = NovixTheme.colors.surface) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello Novix!",
                    style = NovixTheme.typography.headLineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.background(NovixTheme.colors.primary),
                    onClick = {}
                ) {
                    Text(text = "Action")
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun FakeScreenPreviews() {
    NovixTheme {
        FakeScreen()
    }
}
