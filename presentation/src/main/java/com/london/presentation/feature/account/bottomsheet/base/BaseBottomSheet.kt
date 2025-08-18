package com.london.presentation.feature.account.bottomsheet.base

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme


@Composable
fun BaseBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    button: BottomSheetButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp

    val maxHeight = if (isLandscape) {
        screenHeight * 0.85f
    } else {
        screenHeight * 0.75f
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = NovixTheme.colors.surface,
        state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight)
                .padding(bottom = if (isLandscape) 8.dp else 24.dp)
        ) {
            BottomSheetHeader(
                title = title,
                onDismiss = onDismiss,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                content()

                if (button != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            button?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ) {
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
                        BottomSheetButtonType.NONE -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetHeader(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
            painter = painterResource(R.drawable.cancel),
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