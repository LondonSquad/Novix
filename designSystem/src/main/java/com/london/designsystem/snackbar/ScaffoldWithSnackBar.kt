package com.london.designsystem.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.Scaffold
import com.london.designsystem.component.SnackBar
import com.london.designsystem.utils.painter

@Composable
fun DefaultSnackBar(data: SnackBarData) {
    SnackBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = 56.dp),
        title = data.message,
        icon = data.snackBarType.getDefaultIcon().painter
    )
}

@Composable
fun ScaffoldWithSnackBar(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    animationConfig: SnackBarAnimationConfig = SnackBarAnimationConfig(),
    contentColor: Color = contentColorFor(containerColor),
    snackBar: @Composable (data: SnackBarData) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarManager = remember(coroutineScope) { SnackBarControllerImpl(coroutineScope) }
    val currentSnackbarData by snackBarManager.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalSnackbarController provides snackBarManager) {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            containerColor = containerColor,
            contentColor = contentColor
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                content(innerPadding)

                currentSnackbarData.data?.let { data ->
                    AnimatedVisibility(
                        visible = currentSnackbarData.isVisible,
                        enter = animationConfig.enterAnimation,
                        exit = animationConfig.exitAnimation
                    ) {
                        snackBar(data)
                    }
                }
            }
        }
    }
}
