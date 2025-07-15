package com.london.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme

@Composable
fun ErrorImage() {
    val isDarkTheme = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .background(
                color = NovixTheme.colors.surface,
            )
    ) {
        Image(
            painter = painterResource(
                if (isDarkTheme) R.drawable.img_error_dark else R.drawable.img_error_light
            ),
            contentDescription = "Error Image",
            modifier = Modifier
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
        )
    }
}