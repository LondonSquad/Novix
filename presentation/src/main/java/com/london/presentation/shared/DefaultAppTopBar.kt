package com.london.presentation.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.TopBar

@Composable
fun DefaultAppTopBar(
    title: String,
    onBack: () -> Unit,
) {
    TopBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp),
        title = title,
        onBackClick = onBack
    )
}