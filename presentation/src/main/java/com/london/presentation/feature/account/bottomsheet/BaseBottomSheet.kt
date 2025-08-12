package com.london.presentation.feature.account.bottomsheet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.*
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme

@Composable
fun BaseBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    button: BottomSheetButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = NovixTheme.colors.surface,
        state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.75f)
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Header
                BottomSheetHeader(
                    title = title,
                    onDismiss = onDismiss
                )

                // Content
                content()

                // Button (if provided)
                button?.let {
                    when (it.type) {
                        BottomSheetButtonType.PRIMARY -> {
                            PrimaryButton(
                                text = it.text,
                                onClick = it.onClick,
                                isLoading = it.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                                hasIcon = false,
                                hasLabel = true,
                                icon = null
                            )
                        }

                        BottomSheetButtonType.OUTLINE -> {
                            OutlineButton(
                                text = it.text,
                                onClick = it.onClick,
                                isLoading = it.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                                hasIcon = false,
                                hasLabel = true,
                                icon = null
                            )
                        }

                        BottomSheetButtonType.NONE -> { /* No button */
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetHeader(
    title: String,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = NovixTheme.typography.title.large,
            color = NovixTheme.colors.title,
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = NovixTheme.colors.stroke,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = onDismiss)
                .padding(6.dp),
            painter = painterResource(com.london.designsystem.R.drawable.cancel),
            contentDescription = "Close",
            tint = NovixTheme.colors.title
        )
    }
}

enum class BottomSheetButtonType {
    PRIMARY,
    OUTLINE,
    NONE
}

data class BottomSheetButton(
    val text: String,
    val onClick: () -> Unit,
    val type: BottomSheetButtonType = BottomSheetButtonType.PRIMARY,
    val isLoading: Boolean = false
)
