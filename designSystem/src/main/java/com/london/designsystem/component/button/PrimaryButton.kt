package com.london.designsystem.component.button

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews


@Composable
fun PrimaryButton(
    text: String?,
    hasLabel: Boolean,
    @DrawableRes icon: Int?,
    hasIcon: Boolean,
    isLoading: Boolean,
    isDisabled: Boolean,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .defaultMinSize(minWidth = 52.dp)
            .insetShadow(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDisabled) NovixTheme.colors.disable else NovixTheme.colors.primary,
            contentColor = if (isDisabled) NovixTheme.colors.onPrimaryHint else NovixTheme.colors.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {

        if (hasLabel && text != null) {
            Text(
                text = text,
                style = NovixTheme.typography.label.large,
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.width(8.dp))
            LoadingLottieAnimation(
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 8.dp),
                tintColor = NovixTheme.colors.onPrimary
            )
        }

        if (hasIcon && icon != null) {
            if (hasLabel && text != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        }
    }
}

@SuppressLint("SuspiciousModifierThen")
private fun Modifier.insetShadow(
    color: Color = Color(0x1F0D0608),
    offsetX: Dp = 2.dp,
    offsetY: Dp = 4.dp,
    blurRadius: Dp = 12.dp,
    cornerRadius: Dp = 12.dp
) = this.then(
    drawWithCache {
        val paint = Paint().apply {
            this.color = color
            this.blendMode = BlendMode.SrcOver
        }

        val pxOffsetX = offsetX.toPx()
        val pxOffsetY = offsetY.toPx()
        val pxBlurRadius = blurRadius.toPx()
        val pxCornerRadius = cornerRadius.toPx()

        onDrawWithContent {
            drawContent()
            drawIntoCanvas { canvas ->
                canvas.drawRoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = pxCornerRadius,
                    radiusY = pxCornerRadius,
                    paint = paint.apply {
                        asFrameworkPaint().apply {
                            maskFilter = BlurMaskFilter(pxBlurRadius, BlurMaskFilter.Blur.NORMAL)
                        }
                    }
                )
            }
        }
    }
)

@ThemePreviews
@Composable
fun PreviewPrimaryNormal() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = false,
            hasLabel = true,
            icon = R.drawable.icon_add
        )
    }
}

@ThemePreviews
@Composable
fun PreviewPrimaryLoading() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = true,
            isDisabled = false,
            hasIcon = false,
            hasLabel = true,
            icon = null
        )
    }
}

@ThemePreviews
@Composable
fun PreviewPrimaryDisable() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = true,
            hasIcon = false,
            hasLabel = true,
            icon = null
        )
    }
}

@ThemePreviews
@Composable
fun PreviewPrimaryWithTextAndIcon() {
    NovixTheme {
        PrimaryButton(
            text = "Watch",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = true,
            hasLabel = true,
            icon = R.drawable.icon_add
        )
    }
}

@ThemePreviews
@Composable
fun PreviewPrimaryWithIcon() {
    NovixTheme {
        PrimaryButton(
            text = "",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = true,
            hasLabel = false,
            icon = R.drawable.icon_add

        )
    }
}