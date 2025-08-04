package com.london.presentation.feature.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.presentation.R

@Composable
fun ContentRestrictionBottomSheet(
    currentLevel: ContentRestrictionLevel,
    onSaveClick: (ContentRestrictionLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLevel by remember(currentLevel) { mutableStateOf(currentLevel) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.content_restriction),
            style = NovixTheme.typography.title.large,
            color = NovixTheme.colors.title
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContentRestrictionOption(
            level = ContentRestrictionLevel.STRICT,
            title = stringResource(R.string.strict),
            description = stringResource(R.string.blurs_all_sensitive_content),
            isSelected = selectedLevel == ContentRestrictionLevel.STRICT,
            onSelected = { selectedLevel = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContentRestrictionOption(
            level = ContentRestrictionLevel.MODERATE,
            title = stringResource(R.string.moderate),
            description = stringResource(R.string.blurs_explicit_scenes_only),
            isSelected = selectedLevel == ContentRestrictionLevel.MODERATE,
            onSelected = { selectedLevel = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContentRestrictionOption(
            level = ContentRestrictionLevel.OFF,
            title = stringResource(R.string.off),
            description = stringResource(R.string.no_content_is_blurred),
            isSelected = selectedLevel == ContentRestrictionLevel.OFF,
            onSelected = { selectedLevel = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(R.string.save),
            onClick = { onSaveClick(selectedLevel) },
            modifier = Modifier.fillMaxWidth(),
            hasLabel = true,
            hasIcon = false,
            icon = null,
            enabled = true,
            isLoading = false
        )
    }
}

@Composable
private fun ContentRestrictionOption(
    level: ContentRestrictionLevel,
    title: String,
    description: String,
    isSelected: Boolean,
    onSelected: (ContentRestrictionLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) NovixTheme.colors.primaryVariant
                else NovixTheme.colors.surface
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) NovixTheme.colors.primary else NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelected(level) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = NovixTheme.typography.label.large,
                color = NovixTheme.colors.body
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint
            )
        }
    }
}