package com.london.presentation.feature.account.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Selection
import com.london.domain.entity.language.AppLanguage
import com.london.presentation.R
import com.london.presentation.feature.account.bottomsheet.base.BaseBottomSheet
import com.london.presentation.feature.account.bottomsheet.base.BottomSheetButton

@Composable
fun LanguageBottomSheet(
    appLanguage: AppLanguage,
    onEnglishSelected: () -> Unit,
    onArabicSelected: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onLanguageSettingsSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheet(
        title = stringResource(R.string.language),
        onDismiss = onBottomSheetDismiss,
        modifier = modifier,
        button = BottomSheetButton(
            text = stringResource(R.string.save),
            onClick = onLanguageSettingsSave
        )
    ) {
        Selection(
            mainText = stringResource(R.string.english),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            isSelected = (appLanguage == AppLanguage.ENGLISH),
            onClick = onEnglishSelected
        )

        Selection(
            mainText = stringResource(R.string.arabic),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            isSelected = (appLanguage == AppLanguage.ARABIC),
            onClick = onArabicSelected
        )
    }
}
