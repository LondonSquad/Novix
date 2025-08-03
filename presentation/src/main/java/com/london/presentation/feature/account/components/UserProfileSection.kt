package com.london.presentation.feature.account.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.designsystem.R as dsR

@Composable
fun UserProfileSection(
    username: String,
    showUserMenu: Boolean,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserProfileIcon()

                Text(
                    text = username,
                    style = NovixTheme.typography.title.medium,
                    color = NovixTheme.colors.title,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            UserMenuDropdown(
                showUserMenu = showUserMenu,
                onMenuClick = onMenuClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun UserProfileIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NovixTheme.colors.iconBackgroundLow)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(dsR.drawable.user),
            contentDescription = stringResource(R.string.user_profile),
            tint = NovixTheme.colors.hint,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun UserMenuDropdown(
    showUserMenu: Boolean,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box {
        Icon(
            painter = painterResource(R.drawable.more_vertical),
            contentDescription = "More options",
            tint = NovixTheme.colors.body,
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onMenuClick() }
        )

        DropdownMenu(
            expanded = showUserMenu,
            onDismissRequest = onMenuClick,
            modifier = Modifier
                .background(NovixTheme.colors.surface)
                .width(172.dp),
            containerColor = NovixTheme.colors.surface,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = 1.dp,
                color = NovixTheme.colors.stroke
            )
        ) {
            DropdownMenuItem(
                text = {
                    LogoutMenuItem()
                },
                onClick = onLogoutClick,
            )
        }
    }
}

@Composable
private fun LogoutMenuItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(12.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.logout),
            contentDescription = "Logout",
            tint = NovixTheme.colors.redAccent,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = stringResource(R.string.logout),
            style = NovixTheme.typography.label.medium,
            color = NovixTheme.colors.redAccent
        )
    }
}