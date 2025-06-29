package com.london.novix.presentation.component

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.london.novix.R
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews

@Composable
fun ActorItem(
    actorName: String, characterName: String?, imageRes: Any, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageRes,
            contentDescription = "actorImage",
            modifier = Modifier
                .size(78.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp
                    )
                )
                .border(
                    shape = RoundedCornerShape(
                        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp
                    ), width = 1.dp, color = NovixTheme.colors.stroke
                ),
        )
        val color = NovixTheme.colors.stroke
        Column(
            modifier = Modifier
                .align(Alignment.Bottom)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 55.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = color,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height - 1),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(start = 8.dp, top = 4.5.dp, bottom = 4.5.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = actorName,
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.body,
            )
            characterName?.let {
                Text(
                    text = it,
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.hint,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ActorItemPreview() {
    NovixTheme {
        ActorItem(
            actorName = "Lee Jung-jae",
            characterName = "Character name",
            imageRes = R.drawable.frame1597883073
        )
    }
}