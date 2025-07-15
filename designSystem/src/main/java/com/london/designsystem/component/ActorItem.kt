package com.london.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.R
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun ActorItem(
    actorName: String,
    characterName: String?,
    imageRes: Any,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        ActorImage(imageRes = imageRes)
        TextSection(
            actorName = actorName,
            characterName = characterName,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActorImage(imageRes: Any) {
    val isRtl = isRtlLayout()
    val imageShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = if (isRtl) 0.dp else 12.dp,
        bottomEnd = if (isRtl) 12.dp else 0.dp
    )

    Box {
        ImageViewFilter(
            model = imageRes,
            contentDescription = "actorImage",
            modifier = Modifier
                .size(78.dp)
                .clip(shape = imageShape)
                .border(
                    width = 1.dp,
                    shape = imageShape,
                    color = NovixTheme.colors.stroke
                ),
            contentScale = ContentScale.Crop,
            loadingContent = { CircularLoading(modifier = Modifier.align(Alignment.Center)) },
            errorContent = { ErrorImage() },
        )
    }
}

@Composable
fun isRtlLayout(): Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl

@Composable
fun Modifier.customBorder(
    color: Color,
    isRtl: Boolean = isRtlLayout()
): Modifier = this.drawBehind {
    val strokeWidth = 1.dp.toPx()
    val cornerRadius = 12.dp.toPx()

    val left = 0f
    val top = 0f
    val right = size.width
    val bottom = size.height

    val path = Path().apply {
        if (isRtl) {
            moveTo(right, top)
            lineTo(left + cornerRadius, top)
            quadraticTo(left, top, left, top + cornerRadius)
            lineTo(left, bottom - cornerRadius)
            quadraticTo(left, bottom, left + cornerRadius, bottom)
            lineTo(right, bottom)

        } else {
            moveTo(left, top)
            lineTo(right - cornerRadius, top)
            quadraticTo(right, top, right, top + cornerRadius)
            lineTo(right, bottom - cornerRadius)
            quadraticTo(right, bottom, right - cornerRadius, bottom)
            lineTo(left, bottom)
        }
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidth)
    )
}

@Composable
private fun TextSection(
    actorName: String,
    characterName: String?,
    modifier: Modifier = Modifier
) {
    val color = NovixTheme.colors.stroke
    val isRtl = isRtlLayout()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp)
            .customBorder(color = color, isRtl = isRtl)
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = actorName,
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.body,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        characterName?.let {
            Text(
                text = it,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
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
            imageRes = R.drawable.frame1597883073,
            modifier = Modifier.padding(16.dp)
        )
    }
}