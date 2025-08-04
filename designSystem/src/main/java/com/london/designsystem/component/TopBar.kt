package com.london.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

/**
 * When you want to use this TopBar with text only without back button
 * Please apply padding(horizontal = 4.dp) not padding(horizontal = 16.dp)
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    onClickOption1: (() -> Unit)? = null,
    onClickOption2: (() -> Unit)? = null,
    @DrawableRes option1Icon: Int? = null,
    @DrawableRes option2Icon: Int? = null,
    option1IconTint: Color? = null,
    option2IconTint: Color? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .zIndex(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        onBackClick?.let {
            ButtonIcon(
                onClick = it,
                iconRes = R.drawable.arrow_left,
                backgroundColor = NovixTheme.colors.iconBackgroundLow,
                modifier = Modifier.size(40.dp)
            )
        }

        title?.let {
            Text(
                text = title,
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 13.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            onClickOption1?.let {
                ButtonTopBar(option1Icon, onClick = it, iconTint = option1IconTint)
            }
            onClickOption2?.let {
                ButtonTopBar(option2Icon, onClick = it, iconTint = option2IconTint)
            }
        }
    }

}

@Composable
fun ButtonTopBar(
    icon: Int?,
    onClick: () -> Unit,
    iconTint: Color? = null
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = NovixTheme.colors.stroke
            )
            .background(
                color = NovixTheme.colors.iconBackgroundLow
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon ?: R.drawable.add_icon),
            contentDescription = "Back",
            tint = iconTint ?: NovixTheme.colors.title,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
@ThemePreviews
fun TopBarPreview() {
    TopBar(
        onClickOption1 = {},
        onClickOption2 = {}
    )
}