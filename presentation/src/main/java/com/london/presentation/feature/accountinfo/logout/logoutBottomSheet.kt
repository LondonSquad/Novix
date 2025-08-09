package com.london.presentation.feature.accountinfo.logout

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
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R

@Composable
fun LogoutBottomSheet(
    onBottomSheetDismiss: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
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
            LogoutBottomSheetContent(
                onLogout = onLogoutConfirmed,
                onDismiss = onBottomSheetDismiss,
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun LogoutBottomSheetContent(
    onLogout: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.logout),
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

        Text(
            text = stringResource(R.string.are_you_sure_to_continue),
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.body
        )
        OutlineButton(
            text = stringResource(R.string.logout),
            hasLabel = true,
            icon = null,
            hasIcon = false,
            isLoading = isLoading,
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@ThemePreviews
@Composable
fun LogoutBottomSheetContentPreview() {
    NovixTheme {
        LogoutBottomSheetContent(
            onLogout = {},
            onDismiss = {},
            isLoading = false
        )
    }
}