package com.london.presentation.feature.account.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.account.bottomsheet.base.BaseBottomSheet
import com.london.presentation.feature.account.bottomsheet.base.BottomSheetButton
import com.london.presentation.feature.account.bottomsheet.base.BottomSheetButtonType

@Composable
fun LogoutBottomSheet(
    isLoading: Boolean,
    onLogoutConfirmed: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheet(
        title = stringResource(R.string.logout),
        onDismiss = onBottomSheetDismiss,
        modifier = modifier,
        button = BottomSheetButton(
            text = stringResource(R.string.logout),
            onClick = onLogoutConfirmed,
            type = BottomSheetButtonType.OUTLINE,
            isLoading = isLoading
        )
    ) {
        Text(
            text = stringResource(R.string.confirm_action),
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.body,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )
    }
}
