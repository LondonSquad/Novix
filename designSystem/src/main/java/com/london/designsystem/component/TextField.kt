package com.london.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun NovixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isFocused: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String = "",
    label: String? = null,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val currentTextStyle = getAnimatedTextStyle(value, isFocused)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface)
    ) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleSmall,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        NovixTextFieldContainer(
            value = value,
            onValueChange = onValueChange,
            isFocused = isFocused,
            enabled = enabled,
            singleLine = singleLine,
            placeholder = placeholder,
            textStyle = currentTextStyle,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
private fun NovixTextFieldContainer(
    value: String,
    onValueChange: (String) -> Unit,
    isFocused: Boolean,
    enabled: Boolean,
    singleLine: Boolean,
    placeholder: String,
    textStyle: TextStyle,
    leadingIcon: Painter?,
    trailingIcon: Painter?,
    shape: Shape,
    visualTransformation: VisualTransformation
) {
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) NovixTheme.colors.primary else NovixTheme.colors.stroke,
        label = "Border Color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, shape)
            .background(NovixTheme.colors.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let { AnimatedLeadingIcon(it, isFocused) }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            enabled = enabled,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    PlaceholderText(placeholder)
                }
                innerTextField()
            },
            modifier = Modifier.weight(1f)
        )

        trailingIcon?.let { StaticTrailingIcon(it) }
    }
}

@Composable
private fun AnimatedLeadingIcon(painter: Painter, isFocused: Boolean) {
    val iconColor by animateColorAsState(
        targetValue = if (isFocused) NovixTheme.colors.primary else NovixTheme.colors.hint,
        label = "Leading Icon Color"
    )

    Icon(
        painter = painter,
        contentDescription = null,
        tint = iconColor,
        modifier = Modifier
            .size(24.dp)
            .padding(end = 8.dp)
    )
}

@Composable
private fun StaticTrailingIcon(painter: Painter) {
    Icon(
        painter = painter,
        contentDescription = null,
        tint = NovixTheme.colors.hint,
        modifier = Modifier
            .size(20.dp)
            .padding(start = 8.dp)
    )
}

@Composable
private fun PlaceholderText(text: String) {
    Text(
        text = text,
        color = NovixTheme.colors.hint,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun getAnimatedTextStyle(value: String, isFocused: Boolean): TextStyle {
    val targetColor = when {
        isFocused || value.isNotEmpty() -> NovixTheme.colors.body
        else -> NovixTheme.colors.hint
    }

    val animatedColor by animateColorAsState(targetValue = targetColor, label = "Text Color")

    return when {
        isFocused || value.isNotEmpty() -> MaterialTheme.typography.bodyMedium.copy(color = animatedColor)
        else -> MaterialTheme.typography.bodySmall.copy(color = animatedColor)
    }
}

@ThemePreviews
@Composable
fun SampleTextFieldDefaultPreview() {
    NovixTheme {
        var text by remember { mutableStateOf("") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            label = "Title",
            placeholder = "value",
            leadingIcon = painterResource(id = R.drawable.icon_user)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleTextFieldFocusPreview() {
    NovixTheme {
        var text by remember { mutableStateOf("Sample Text") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            label = "Title",
            placeholder = "value",
            isFocused = true,
            leadingIcon = painterResource(id = R.drawable.icon_user)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleTextFieldWithoutIconPreview() {
    NovixTheme {
        var text by remember { mutableStateOf("") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            label = "Title",
            placeholder = "value"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleTextFieldWithoutTitlePreview() {
    NovixTheme {
        var text by remember { mutableStateOf("") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "value"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleTextFieldWithoutHintPreview() {
    NovixTheme {
        var text by remember { mutableStateOf("") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            label = "Title"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleTextPasswordPreview() {
    NovixTheme {
        var text by remember { mutableStateOf("password123") }
        NovixTextField(
            value = text,
            onValueChange = { text = it },
            label = "Password",
            placeholder = "Enter password",
            trailingIcon = painterResource(id = R.drawable.icon_password),
        )
    }
}
