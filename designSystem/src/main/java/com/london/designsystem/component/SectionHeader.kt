package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.noRippleClickable
import com.london.designsystem.utils.shimmerEffect

@Composable
fun SectionHeader(
    text: String,
    hasGetAll: Boolean,
    hasIcon: Boolean,
    modifier: Modifier = Modifier,
    getAllText: String = stringResource(R.string.all),
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = NovixTheme.colors.surface)
    ) {
        if (!isLoading)
            Text(
                text = text,
                style = NovixTheme.typography.headline.small,
                color = NovixTheme.colors.title,
            )else
                Box(
            modifier = Modifier
                .height(35.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
        if (hasGetAll && !isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.noRippleClickable(onClick),
            ) {
                Text(
                    text = getAllText,
                    style = NovixTheme.typography.label.medium,
                    color = NovixTheme.colors.primary,
                )
                if (hasIcon) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow),
                        contentDescription = stringResource(R.string.arrow),
                        tint = NovixTheme.colors.primary
                    )
                }
            }
        }else if (isLoading){
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}

@ThemePreviews
@Composable
fun SectionHeaderPreview() {
    NovixTheme {
        SectionHeader(
            text = stringResource(R.string.new_arrival),
            hasGetAll = true,
            hasIcon = true
        )
    }
}