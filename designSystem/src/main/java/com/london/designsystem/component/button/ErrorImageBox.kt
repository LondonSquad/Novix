package com.london.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R

@Composable
fun ErrorImage(isDarkMode: Boolean) {
    Image(
        painter = painterResource(
            if (isDarkMode) R.drawable.img_error_dark else R.drawable.img_error_light
        ),
        contentDescription = stringResource(R.string.error_image),
        modifier = Modifier.size(56.dp)
    )
}