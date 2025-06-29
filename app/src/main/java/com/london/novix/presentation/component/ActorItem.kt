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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
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
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageRes,
            contentDescription = "actorImage",
            modifier = Modifier
                .size(78.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp
                    )
                )
                .border(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp
                    ), width = 1.dp,
                    color = NovixTheme.colors.stroke
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
                    val cornerRadius = 12.dp.toPx()

                    val left = 0f
                    val top = 0f
                    val right = size.width
                    val bottom = size.height

                    val path =Path().apply {
                        moveTo(left, top)

                        lineTo(right - cornerRadius, top)
                        quadraticTo(right, top, right, top + cornerRadius)

                        lineTo(right, bottom - cornerRadius)
                        quadraticTo(right, bottom, right - cornerRadius, bottom)

                        lineTo(left, bottom - 1)
                        quadraticTo(left, bottom, left + cornerRadius, bottom)
                    }
                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = strokeWidth)
                    )
                }
                .padding(
                    start = 8.dp,
                    top = 4.5.dp,
                    bottom = 4.5.dp
                ),
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