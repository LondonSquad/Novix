package com.london.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.painter

@Composable
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    appName: String,
    appDescription: String,
    @DrawableRes appIconRes: Int,
    appIconContentDescription: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        Image(
            painter = appIconRes.painter,
            contentDescription = appIconContentDescription,
            modifier = Modifier.size(48.dp)
        )
        Column {
            Text(
                text = appName,
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.body
            )
            Text(
                text = appDescription,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint
            )
        }
    }
}

@Composable
@ThemePreviews
fun DefaultPreview() {
    DefaultTopBar(
        appName = "Novix",
        appDescription = "Born from Nova, Made for Flix.",
        appIconRes = R.drawable.novix_icon,
        appIconContentDescription = "Logo"
    )
}