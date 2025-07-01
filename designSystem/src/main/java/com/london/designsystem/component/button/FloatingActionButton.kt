package com.london.designsystem.component.button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun FloatingActionButton(
    isDefaultIcon: Boolean,
    isLoadingIcon: Boolean,
    isDisabledIcon: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(56.dp),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDisabledIcon) NovixTheme.colors.disable else NovixTheme.colors.primary,
            contentColor = if (isDisabledIcon) NovixTheme.colors.onPrimaryHint else NovixTheme.colors.onPrimary
        )
    ) {
        if (isDisabledIcon || isDefaultIcon) {
            Icon(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Add button",
                modifier = Modifier.size(24.dp)
            )
        }


        if (isLoadingIcon) {
            LoadingLottieAnimation(
                modifier = Modifier
                    .size(24.dp),
                tintColor = NovixTheme.colors.onPrimary
            )
        }
    }
}

@ThemePreviews
@Composable
fun DefaultFloatingActionButtonPreview() {
    NovixTheme {
        FloatingActionButton(
            modifier = Modifier.size(100.dp),
            onClick = {},
            isLoadingIcon = false,
            isDisabledIcon = false,
            isDefaultIcon = true
        )
    }
}

@ThemePreviews
@Composable
fun LoadingFloatingActionButtonPreview() {
    NovixTheme {
        FloatingActionButton(
            modifier = Modifier.size(100.dp),
            onClick = {},
            isLoadingIcon = true,
            isDisabledIcon = false,
            isDefaultIcon = false
        )
    }
}

@ThemePreviews
@Composable
fun DisabledFloatingActionButtonPreview() {
    NovixTheme {
        FloatingActionButton(
            modifier = Modifier.size(100.dp),
            onClick = {},
            isLoadingIcon = false,
            isDisabledIcon = true,
            isDefaultIcon = false
        )
    }
}