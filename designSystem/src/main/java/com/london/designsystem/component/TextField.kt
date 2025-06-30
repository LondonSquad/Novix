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
            focusedBorderColor = Color(0xFFF77053),
            unfocusedBorderColor = Color(0xFF3A3A3A),
            focusedLabelColor = Color(0xFFF77053),
            unfocusedLabelColor = Color(0xFFAAAAAA),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            disabledTextColor = Color.Gray,
            disabledBorderColor = Color(0xFF444444)
        )
    )
}

@Composable
fun SampleTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    NovixTextField(
        value = text,
        onValueChange = { text = it },
        label = "Title",
        placeholder = "value",
        leadingIcon = painterResource(id = R.drawable.icon_arrow)
    )
}