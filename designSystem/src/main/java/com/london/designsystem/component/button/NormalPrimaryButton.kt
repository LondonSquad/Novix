package com.london.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.typography.NovixFont


@Composable
fun NormalPrimaryButton(
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
            .defaultMinSize(minWidth = 79.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDisabled) NovixTheme.colors.disable else NovixTheme.colors.primary,
            contentColor = if (isDisabled) NovixTheme.colors.onPrimaryHint else NovixTheme.colors.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.onPrimary,
            fontFamily = NovixFont,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        if (isLoading) {
            Spacer(modifier = Modifier.width(8.dp))
            LoadingLottieAnimation(
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 8.dp),
                tintColor = NovixTheme.colors.onPrimary
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
fun PreviewPrimaryNormal() {
    NovixTheme {
        NormalPrimaryButton(
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
fun PreviewPrimaryLoading() {
    NovixTheme {
        NormalPrimaryButton(
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
fun PreviewPrimaryDisable() {
    NovixTheme {
        NormalPrimaryButton(
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
fun PreviewPrimaryWithIcon() {
    NovixTheme {
        NormalPrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = true
        )
    }
}