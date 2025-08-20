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
    onBackClick: () -> Unit,
) {
    TopBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        title = title,
        onBackClick = onBackClick
    )
}
