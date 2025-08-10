package com.london.presentation.feature.list.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Scaffold
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.R

@Composable
fun BookmarksScreen(modifier: Modifier = Modifier) {
    Scaffold(containerColor = NovixTheme.colors.surface) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = R.string.my_list_screen.string,
                style = NovixTheme.typography.headline.medium
            )
        }
    }
}