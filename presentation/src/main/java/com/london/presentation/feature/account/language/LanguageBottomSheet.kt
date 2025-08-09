package com.london.presentation.feature.account.language

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
import com.london.domain.language.AppLanguage
import com.london.presentation.R

@Composable
fun LanguageBottomSheet(
    appLanguage: AppLanguage,
    onBottomSheetDismiss: () -> Unit,
    onEnglishSelected: () -> Unit,
    onArabicSelected: () -> Unit,
    onLanguageSettingsSave: () -> Unit,
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
            LanguageBottomSheetContent(
                onEnglish = onEnglishSelected,
                onArabic = onArabicSelected,
                onSave = onLanguageSettingsSave,
                onDismiss = onBottomSheetDismiss,
                appLanguage = appLanguage
            )
        }
    }
}

@Composable
fun LanguageBottomSheetContent(
    onEnglish: () -> Unit,
    onArabic: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    appLanguage: AppLanguage,
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
                text = stringResource(R.string.language),
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
                contentDescription = stringResource(R.string.close),
                tint = NovixTheme.colors.title
            )
        }

        Selection(
            mainText = stringResource(R.string.english),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            isSelected = (appLanguage == AppLanguage.ENGLISH),
            onClick = onEnglish
        )

        Selection(
            mainText = stringResource(R.string.arabic),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            isSelected = (appLanguage == AppLanguage.ARABIC),
            onClick = onArabic
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
fun LanguageBottomSheetContentPreview() {
    LanguageBottomSheetContent(
        onEnglish = {},
        onArabic = {},
        onSave = {},
        onDismiss = {},
        appLanguage = AppLanguage.ENGLISH,
    )
}