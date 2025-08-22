package com.london.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
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
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDisabledIcon) NovixTheme.colors.disable else NovixTheme.colors.primary,
            contentColor = if (isDisabledIcon) NovixTheme.colors.onPrimaryHint else NovixTheme.colors.onPrimary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isDisabledIcon || isDefaultIcon) {
            Icon(
                painter = painterResource(id = R.drawable.add_icon),
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
private fun DefaultFloatingActionButtonPreview() {
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
private fun LoadingFloatingActionButtonPreview() {
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
private fun DisabledFloatingActionButtonPreview() {
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
