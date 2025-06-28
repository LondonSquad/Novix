package com.london.novix.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.london.novix.R
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews

@Composable
fun ActorItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String?,
    image: Any
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .size(78.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp
                    )
                )
                .border(width = 1.dp, color = NovixTheme.colors.stroke),
        )
        Column(
            modifier = Modifier
                .align(Alignment.Bottom)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 55.dp)
                .border(width = 1.dp, color = NovixTheme.colors.stroke)
                .padding(start = 8.dp),
            verticalArrangement = if (subtitle == null) Arrangement.Center
            else Arrangement.Top,

            ) {
            Text(
                text = title,
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.body,
                modifier = Modifier.padding(top = if (subtitle == null) 0.dp else 4.dp)
            )
            subtitle?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(bottom = 4.5.dp),
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.hint,
                    )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ActorsCardPreview() {
    NovixTheme {
        ActorItem(title = "Lee Jung-jae",
            subtitle = "Character name",
            image = R.drawable.frame1597883073
        )
    }
}