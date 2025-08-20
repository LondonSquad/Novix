package com.london.presentation.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.TopBar
import com.london.presentation.utils.detailsTopBar

@Composable
fun DefaultAppTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    TopBar(
        modifier = Modifier.detailsTopBar(1f),
        title = title,
        onBackClick = onBackClick
    )
}
