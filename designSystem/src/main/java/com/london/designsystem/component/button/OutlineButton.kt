package com.london.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
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
    text: String?,
    hasLabel: Boolean,
    @DrawableRes icon: Int?,
    hasIcon: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, NovixTheme.colors.stroke),
        colors = ButtonDefaults.buttonColors(
            containerColor = NovixTheme.colors.surface,
            contentColor = NovixTheme.colors.primary,
            disabledContentColor = NovixTheme.colors.disable
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
            .heightIn(48.dp)
            .defaultMinSize(minWidth = 78.dp)
    ) {
        if (hasLabel && text != null) {
            Text(
                text = text,
                style = NovixTheme.typography.label.large,
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.width(8.dp))
            LoadingLottieAnimation(
                modifier = Modifier
                    .size(24.dp),
                tintColor = NovixTheme.colors.primary
            )
        }
        if (hasIcon && icon != null) {
            if (hasLabel && text != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                painter = painterResource(icon),
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
            hasIcon = false,
            hasLabel = true,
            icon = R.drawable.icon_add
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
            hasIcon = false,
            hasLabel = true,
            icon = null
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
            enabled = false,
            hasIcon = false,
            hasLabel = true,
            icon = null
        )
    }
}

@ThemePreviews
@Composable
fun PreviewOutlinePrimaryWithIcon() {
    NovixTheme {
        OutlineButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            hasIcon = true,
            hasLabel = true,
            icon = R.drawable.icon_add
        )
    }
}

@ThemePreviews
@Composable
fun PreviewPrimaryWithIconOnly() {
    NovixTheme {
        PrimaryButton(
            text = "",
            onClick = {},
            isLoading = false,
            hasIcon = true,
            hasLabel = false,
            icon = R.drawable.icon_add

        )
    }
}