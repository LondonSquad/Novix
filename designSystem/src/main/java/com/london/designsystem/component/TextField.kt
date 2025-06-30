package com.london.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun NovixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    singleLine: Boolean = true,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null
                )
            }
        },
        shape = shape,
        isError = isError,
        enabled = enabled,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NovixTheme.colors.primary,
            unfocusedBorderColor = NovixTheme.colors.stroke,
            focusedLabelColor = NovixTheme.colors.primary,
            unfocusedLabelColor = NovixTheme.colors.hint,
            focusedTextColor = NovixTheme.colors.body,
            unfocusedTextColor = NovixTheme.colors.hint,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            disabledTextColor =  NovixTheme.colors.hint,
            disabledBorderColor = NovixTheme.colors.hint
        )
    )
}

@ThemePreviews
@Composable
fun SampleTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    NovixTextField(
        value = text,
        onValueChange = { text = it },
        label = "Title",
        placeholder = "value",
        leadingIcon = painterResource(id = R.drawable.icon_user)
    )
}