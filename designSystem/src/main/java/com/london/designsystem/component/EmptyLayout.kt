package com.london.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

@Composable
fun EmptyLayout(
    text: String,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    additionalContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Search Icon",
            modifier = Modifier
                .size(128.dp)
                .align(CenterHorizontally),
            contentScale = ContentScale.Fit
        )
        Text(
            text = text,
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        additionalContent?.let { it() }
    }
}