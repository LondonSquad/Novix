package com.london.designsystem.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.Scaffold
import com.london.designsystem.component.SnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SnackBarData(
    val message: String,
    @DrawableRes val icon: Int?,
    val snackBarType: SnackBarType,
)

private class SnackBarManager(
    private val coroutineScope: CoroutineScope
) : SnackBarController {
    val snackBarData = mutableStateOf<SnackBarData?>(null)
    private var job: Job? = null

    override fun showMessage(
        message: String,
        icon: Int?,
        snackBarType: SnackBarType,
        onComplete: () -> Unit
    ) {
        job?.cancel()
        job = coroutineScope.launch {
            snackBarData.value = SnackBarData(message, icon, snackBarType)
            delay(3000L)
            onComplete()
            snackBarData.value = null
        }
    }
}

@Composable
fun CustomSnackBarUI(data: SnackBarData) {
    SnackBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = 56.dp),
        title = data.message,
        icon = painterResource(data.icon ?: R.drawable.ic_failed)
    )
}

@Composable
fun ScaffoldWithSnackBar(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    snackBar: @Composable (data: SnackBarData) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarManager = remember(coroutineScope) { SnackBarManager(coroutineScope) }
    val currentSnackbarData by snackBarManager.snackBarData

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

                AnimatedVisibility(
                    visible = currentSnackbarData != null,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween()
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween()
                    )
                ) {
                    currentSnackbarData?.let { snackBar(it) }
                }
            }
        }
    }
}
