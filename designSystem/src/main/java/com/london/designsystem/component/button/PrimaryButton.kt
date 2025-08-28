package com.london.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
fun PrimaryButton(
    text: String?,
    hasLabel: Boolean,
    @DrawableRes icon: Int?,
    hasIcon: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(48.dp)
            .defaultMinSize(minWidth = 52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NovixTheme.colors.primary,
            contentColor = NovixTheme.colors.onPrimary,
            disabledContainerColor = NovixTheme.colors.disable,
            disabledContentColor = NovixTheme.colors.onPrimaryHint
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    .size(20.dp)
                    .padding(start = 8.dp),
                tintColor = NovixTheme.colors.onPrimary
            )
        }

        if (hasIcon && icon != null) {
            if (hasLabel && text != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PreviewPrimaryNormal() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            enabled = false,
            hasIcon = false,
            hasLabel = true,
            icon = R.drawable.add_icon
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewPrimaryLoading() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = true,
            enabled = false,
            hasIcon = false,
            hasLabel = true,
            icon = null
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewPrimaryDisable() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            enabled = true,
            hasIcon = false,
            hasLabel = true,
            icon = null
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewPrimaryWithTextAndIcon() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            enabled = false,
            hasIcon = true,
            hasLabel = true,
            icon = R.drawable.add_icon
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewPrimaryWithIcon() {
    NovixTheme {
        PrimaryButton(
            text = "",
            onClick = {},
            isLoading = false,
            enabled = false,
            hasIcon = true,
            hasLabel = false,
            icon = R.drawable.add_icon

        )
    }
}
