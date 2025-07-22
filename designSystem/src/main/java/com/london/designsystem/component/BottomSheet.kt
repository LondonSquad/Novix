package com.london.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

class SheetState(val sheetState: androidx.compose.material3.SheetState) {
    suspend fun show() = sheetState.show()
    suspend fun hide() = sheetState.hide()
    val isVisible: Boolean get() = sheetState.isVisible
    val currentValue: SheetValue get() = sheetState.currentValue
    val targetValue: SheetValue get() = sheetState.targetValue
    val hasExpanded: Boolean get() = sheetState.hasExpandedState
    val hasPartiallyExpanded: Boolean get() = sheetState.hasPartiallyExpandedState
}

@Composable
fun rememberNovixModalBottomSheetState(
    skipPartiallyExpanded: Boolean = true
): SheetState {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    return remember { SheetState(sheetState) }
}

@Composable
fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    state: SheetState,
    modifier: Modifier = Modifier,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    content: @Composable ColumnScope.() -> Unit,
) {
    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = state.sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = NovixTheme.colors.body,
            )
        },
        contentWindowInsets = contentWindowInsets,
        properties = ModalBottomSheetDefaults.properties,
        content = content,
    )
}