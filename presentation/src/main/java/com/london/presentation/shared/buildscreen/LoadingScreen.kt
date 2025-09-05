package com.london.presentation.shared.buildscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.BackgroundGradient

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BackgroundGradient(
            modifier = Modifier
                .align(Alignment.TopStart)
        )

        CircularLoading()
    }
}
