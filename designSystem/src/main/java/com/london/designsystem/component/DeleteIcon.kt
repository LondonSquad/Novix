package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.noRippleClickable

@Composable
fun DeleteIcon(
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = NovixTheme.colors.iconBackground)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(6.dp)
            .noRippleClickable {
                onDeleteClick()
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = "delete",
            tint = NovixTheme.colors.redAccent,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@ThemePreviews
@Composable
fun DeleteIconPreview() {
    NovixTheme {
        DeleteIcon(
            onDeleteClick = {}
        )
    }
}