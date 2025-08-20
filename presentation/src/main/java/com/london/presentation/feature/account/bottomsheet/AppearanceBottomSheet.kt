package com.london.presentation.feature.account.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Selection
import com.london.domain.entity.theme.AppTheme
import com.london.presentation.R
import com.london.presentation.feature.account.bottomsheet.base.BaseBottomSheet
import com.london.presentation.feature.account.bottomsheet.base.BottomSheetButton

@Composable
fun AppearanceBottomSheet(
    appTheme: AppTheme,
    onDarkModeSelected: () -> Unit,
    onLightModeSelected: () -> Unit,
    onAppearanceModeSave: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        title = stringResource(R.string.appearance),
        onDismiss = onBottomSheetDismiss,
        modifier = modifier,
        button = BottomSheetButton(
            text = stringResource(R.string.save),
            onClick = onAppearanceModeSave
        )
    ) {
        Selection(
            mainText = stringResource(R.string.dark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            isSelected = (appTheme == AppTheme.DARK),
            onClick = onDarkModeSelected
        )

        Selection(
            mainText = stringResource(R.string.light),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            isSelected = (appTheme == AppTheme.LIGHT),
            onClick = onLightModeSelected
        )
    }
}
