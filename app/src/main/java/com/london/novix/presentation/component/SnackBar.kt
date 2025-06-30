package com.london.novix.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.novix.R
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews

@Composable
fun SnackBar(
    title: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier
            .background(color = NovixTheme.colors.surface)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(end = 8.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = title,
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.title,
        )
    }
}

@ThemePreviews
@Composable
private fun SuccessSnackBarPreview() {
    NovixTheme {
        SnackBar(
            title = stringResource(R.string.rate_submitted_successfully),
            icon = painterResource(R.drawable.ic_success)
        )
    }
}

@ThemePreviews
@Composable
private fun FailSnackBarPreview() {
    NovixTheme {
        SnackBar(
            title = stringResource(R.string.error_submitting_rate),
            icon = painterResource(R.drawable.ic_failed)
        )
    }
}