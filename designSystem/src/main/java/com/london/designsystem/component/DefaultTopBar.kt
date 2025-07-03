package com.london.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun DefaultTopBar(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
}

@Composable
@ThemePreviews
fun DefaultPreview() {
    DefaultTopBar(
    )
}