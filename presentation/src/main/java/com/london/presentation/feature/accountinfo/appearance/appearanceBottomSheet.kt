package com.london.presentation.feature.accountinfo.appearance

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.Selection
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.theme.AppTheme
import com.london.presentation.R

@Composable
fun AppearanceBottomSheet(
    appTheme: AppTheme,
    onBottomSheetDismiss: () -> Unit,
    onDarkModeSelected: () -> Unit,
    onLightModeSelected: () -> Unit,
    onAppearanceModeSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onBottomSheetDismiss,
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
            AppearanceBottomSheetContent(
                onDark = onDarkModeSelected,
                onLight = onLightModeSelected,
                onSave = onAppearanceModeSave,
                onDismiss = onBottomSheetDismiss,
                appTheme = appTheme
            )
        }
    }
}

@Composable
fun AppearanceBottomSheetContent(
    onDark: () -> Unit,
    onLight: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    appTheme: AppTheme,

) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.appearance),
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
                contentDescription = "Close filter",
                tint = NovixTheme.colors.title
            )
        }

        Selection(
            mainText = stringResource(R.string.dark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            isSelected =
                (appTheme == AppTheme.DARK),
            onClick = onDark
        )

        Selection(
            mainText = stringResource(R.string.light),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            isSelected =
                (appTheme == AppTheme.LIGHT),
            onClick = onLight
        )

        PrimaryButton(
            text = stringResource(R.string.save),
            hasIcon = false,
            hasLabel = true,
            icon = null,
            isLoading = false,
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@ThemePreviews
@Composable
fun AppearanceBottomSheetContentPreview() {
    AppearanceBottomSheetContent(
        onDark = {},
        onLight = {},
        onSave = {},
        onDismiss = {},
        appTheme = AppTheme.DARK
    )
}