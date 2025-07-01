package com.london.novix.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun TopBar(
    type: TopBarType = TopBarType.Default,
    title: String = "My account",
    onBackClick: (() -> Unit),
    option1: (() -> Unit)? = null,
    option2: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when (type) {
            TopBarType.Default -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.icondesign),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.appName),
                            style = NovixTheme.typography.title.medium,
                            color = NovixTheme.colors.body
                        )
                        Text(
                            text = stringResource(R.string.explainTheNameOfApp),
                            style = NovixTheme.typography.label.small,
                            color = NovixTheme.colors.hint
                        )
                    }
                }
            }

            else -> {
                if (type != TopBarType.WithoutBackArrow) {
                    ButtonTopBar(icon = R.drawable.arrow_left, onClick = onBackClick)
                }
            }
        }

        if (type != TopBarType.Default && type != TopBarType.WithoutScreenTitle) {
            Text(
                text = title,
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
        } else if (type == TopBarType.WithoutScreenTitle) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (type == TopBarType.WithOneOption) {
                option1?.let {
                    ButtonTopBar(R.drawable.add_icon, onClick = it)
                }
            }
            if (type == TopBarType.WithTwoOptions) {
                option1?.let {
                    ButtonTopBar(R.drawable.add_icon, onClick = it)
                }
                option2?.let {
                    ButtonTopBar(R.drawable.pencil_edit, onClick = it)
                }
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

enum class TopBarType {
    Default,
    AppBar,
    WithOneOption,
    WithTwoOptions,
    WithoutBackArrow,
    WithoutScreenTitle
}


@Composable
@ThemePreviews
fun TopBarPreview() {
    TopBar(
        type = TopBarType.WithTwoOptions,
        onBackClick = {},
        option1 = {},
        option2 = {}
    )
}