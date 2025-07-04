package com.london.designsystem.component.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun OutlineButton(
    text: String,
    hasIcon: Boolean,
    isLoading: Boolean,
    isDisabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .defaultMinSize(minWidth = 79.dp)
            .border(1.dp, color = NovixTheme.colors.stroke),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NovixTheme.colors.surface,
            contentColor = if (isDisabled) NovixTheme.colors.disable else NovixTheme.colors.primary
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            style = NovixTheme.typography.label.large,
        )

        if (isLoading) {
            Spacer(modifier = Modifier.width(8.dp))
            LoadingLottieAnimation(
                modifier = Modifier
                    .size(24.dp),
                tintColor = NovixTheme.colors.primary
            )
        }

        if (hasIcon) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = null
            )
        }
    }
}

@ThemePreviews
@Composable
fun PreviewNormalOutlineButton() {
    NovixTheme {
        OutlineButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = false
        )
    }
}

@ThemePreviews
@Composable
fun PreviewLoadingOutlineButton() {
    NovixTheme {
        OutlineButton(
            text = "Watch",
            onClick = {},
            isLoading = true,
            isDisabled = false,
            hasIcon = false
        )
    }
}

@ThemePreviews
@Composable
fun PreviewDisabledPrimaryDisable() {
    NovixTheme {
        OutlineButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = true,
            hasIcon = false
        )
    }
}

@ThemePreviews
@Composable
fun PreviewWithIconPrimaryWithIcon() {
    NovixTheme {
        OutlineButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = true
        )
    }
}