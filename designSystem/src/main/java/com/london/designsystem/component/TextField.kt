package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val currentTextStyle = getTextStyle(value, isFocused)

    Column(modifier = modifier
        .fillMaxWidth()
        .background(NovixTheme.colors.surface)) {
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
            currentTextStyle = currentTextStyle,
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
    currentTextStyle: TextStyle,
    leadingIcon: Painter?,
    trailingIcon: Painter?,
    shape: Shape,
    visualTransformation: VisualTransformation
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isFocused) NovixTheme.colors.primary else NovixTheme.colors.stroke,
                shape = shape
            )
            .background(NovixTheme.colors.surface)
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let { LeadingIcon(it, isFocused) }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            enabled = enabled,
            textStyle = currentTextStyle,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    PlaceholderText(placeholder)
                }
                innerTextField()
            },
            modifier = Modifier.weight(1f)
        )

        trailingIcon?.let { TrailingIcon(it) }
    }
}

@Composable
private fun LeadingIcon(painter: Painter, isFocused: Boolean) {
    Icon(
        painter = painter,
        contentDescription = null,
        tint = if (isFocused) NovixTheme.colors.primary else NovixTheme.colors.hint,
        modifier = Modifier
            .size(24.dp)
            .padding(end = 8.dp)
    )
}

@Composable
private fun TrailingIcon(painter: Painter) {
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
private fun PlaceholderText(placeholder: String) {
    Text(
        text = placeholder,
        color = NovixTheme.colors.hint,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun getTextStyle(value: String, isFocused: Boolean): TextStyle {
    return when {
        isFocused -> MaterialTheme.typography.bodyMedium.copy(color = NovixTheme.colors.body)
        value.isNotEmpty() -> MaterialTheme.typography.bodyMedium.copy(color = NovixTheme.colors.body)
        else -> MaterialTheme.typography.bodySmall.copy(color = NovixTheme.colors.hint)
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
