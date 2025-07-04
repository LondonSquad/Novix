package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    option1: (() -> Unit)? = null,
    option2: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        onBackClick?.let { ButtonTopBar(icon = R.drawable.arrow_left, onClick = {}) }

        title?.let {
            Text(
                text = title,
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            option1?.let {
                ButtonTopBar(R.drawable.add_icon, onClick = it)
            }
            option2?.let {
                ButtonTopBar(R.drawable.pencil_edit, onClick = it)
            }
        }
    }
}

@Composable
fun ButtonTopBar(icon: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(end = 8.dp)
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NovixTheme.colors.iconBackgroundLow)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Back",
            tint = NovixTheme.colors.title
        )
    }
}

@Composable
@ThemePreviews
fun TopBarPreview() {
    TopBar(
        option1 = {},
        option2 = {}
    )
}
