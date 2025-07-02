package com.london.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = NovixTheme.typography.body.medium,
    label: String = "",
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: Painter? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(all = 12.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: TextFieldColors = getDefaultTextFieldColors(),
    backgroundColor: Color = NovixTheme.colors.surface,
    isPasswordField: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {},
    passwordVisibleIcon: Painter? = null,
    passwordHiddenIcon: Painter? = null,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val mergedTextStyle = textStyle.merge(TextStyle(color = NovixTheme.colors.body))
    val isPasswordEmpty = value.text.isEmpty()

    val currentVisualTransformation = getVisualTransformation(
        isPasswordField = isPasswordField,
        passwordVisible = passwordVisible,
        isPasswordEmpty = isPasswordEmpty,
        defaultTransformation = visualTransformation
    )

    val currentTrailingIcon = getTrailingIcon(
        isPasswordField = isPasswordField,
        isPasswordEmpty = isPasswordEmpty,
        passwordVisible = passwordVisible,
        passwordVisibleIcon = passwordVisibleIcon,
        passwordHiddenIcon = passwordHiddenIcon,
        onPasswordVisibilityChange = onPasswordVisibilityChange,
        defaultTrailingIcon = trailingIcon
    )

    TextFieldContainer(
        modifier = modifier,
        backgroundColor = backgroundColor,
        label = label,
        content = {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minWidth = 268.dp, minHeight = 48.dp)
                    .background(backgroundColor),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = mergedTextStyle,
                visualTransformation = currentVisualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                singleLine = singleLine,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value.text,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder,
                        leadingIcon = null,
                        trailingIcon = null,
                        prefix = leadingIcon?.let { { AnimatedLeadingIcon(it, isFocused) } },
                        suffix = currentTrailingIcon,
                        supportingText = supportingText,
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        contentPadding = contentPadding,
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = shape,
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun getDefaultTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = NovixTheme.colors.primary,
        unfocusedBorderColor = NovixTheme.colors.stroke,
        focusedTextColor = NovixTheme.colors.body,
        unfocusedTextColor = NovixTheme.colors.body,
        focusedPlaceholderColor = NovixTheme.colors.hint,
        unfocusedPlaceholderColor = NovixTheme.colors.hint,
    )
}

private fun getVisualTransformation(
    isPasswordField: Boolean,
    passwordVisible: Boolean,
    isPasswordEmpty: Boolean,
    defaultTransformation: VisualTransformation
): VisualTransformation {
    return when {
        isPasswordField && !passwordVisible && !isPasswordEmpty -> PasswordVisualTransformation()
        else -> defaultTransformation
    }
}

@Composable
private fun getTrailingIcon(
    isPasswordField: Boolean,
    isPasswordEmpty: Boolean,
    passwordVisible: Boolean,
    passwordVisibleIcon: Painter?,
    passwordHiddenIcon: Painter?,
    onPasswordVisibilityChange: () -> Unit,
    defaultTrailingIcon: @Composable (() -> Unit)?
): @Composable (() -> Unit)? {
    return when {
        isPasswordField && passwordVisibleIcon != null && passwordHiddenIcon != null -> {
            {
                PasswordToggleIcon(
                    isPasswordEmpty = isPasswordEmpty,
                    passwordVisible = passwordVisible,
                    passwordVisibleIcon = passwordVisibleIcon,
                    passwordHiddenIcon = passwordHiddenIcon,
                    onToggle = onPasswordVisibilityChange
                )
            }
        }

        else -> defaultTrailingIcon
    }
}

@Composable
private fun TextFieldContainer(
    modifier: Modifier,
    backgroundColor: Color,
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = NovixTheme.typography.title.small,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        content()
    }
}

@Composable
private fun AnimatedLeadingIcon(
    painter: Painter,
    isFocused: Boolean
) {
    val iconColor by animateColorAsState(
        targetValue = if (isFocused) NovixTheme.colors.primary else NovixTheme.colors.hint,
        label = "Leading Icon Color"
    )
    Icon(
        painter = painter,
        contentDescription = "Leading Icon",
        tint = iconColor,
        modifier = Modifier
            .size(24.dp)
            .padding(end = 8.dp)
    )
}

@Composable
private fun PasswordToggleIcon(
    isPasswordEmpty: Boolean,
    passwordVisible: Boolean,
    passwordVisibleIcon: Painter,
    passwordHiddenIcon: Painter,
    onToggle: () -> Unit
) {
    if (isPasswordEmpty) {
        Icon(
            painter = passwordHiddenIcon,
            contentDescription = "Password field empty",
            tint = NovixTheme.colors.hint,
            modifier = Modifier.size(20.dp)
        )
    } else {
        IconButton(onClick = onToggle) {
            Icon(
                painter = if (passwordVisible) passwordVisibleIcon else passwordHiddenIcon,
                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                tint = NovixTheme.colors.hint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun TextFieldDefaultPreview() {
    var value by remember { mutableStateOf(TextFieldValue("Preview value")) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = "Text Field",
            placeholder = {
                Text(
                    "Enter your text",
                    style = NovixTheme.typography.body.small
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_user),
            interactionSource = interactionSource,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun PasswordFieldEmptyPreview() {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = "Password",
            placeholder = {
                Text(
                    "Enter your password",
                    style = NovixTheme.typography.body.small
                )
            },
            isPasswordField = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
            passwordVisibleIcon = painterResource(id = R.drawable.icon_eye_open),
            passwordHiddenIcon = painterResource(id = R.drawable.icon_eye_closed),
            interactionSource = interactionSource,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun PasswordFieldFilledPreview() {
    var value by remember { mutableStateOf(TextFieldValue("SecurePassword123")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = "Password",
            placeholder = {
                Text(
                    "Enter your password",
                    style = NovixTheme.typography.body.small
                )
            },
            isPasswordField = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
            passwordVisibleIcon = painterResource(id = R.drawable.icon_eye_open),
            passwordHiddenIcon = painterResource(id = R.drawable.icon_eye_closed),
            interactionSource = interactionSource,
            supportingText = {
                Text(text = "Password must be at least 8 characters")
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun TextFieldErrorStatePreview() {
    var value by remember { mutableStateOf(TextFieldValue("invalid-email")) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = "Text",
            placeholder = {
                Text(
                    "Enter your text",
                    style = NovixTheme.typography.body.small
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_user),
            isError = true,
            interactionSource = interactionSource,
            supportingText = {
                Text(
                    text = "Please enter a valid email address",
                    color = MaterialTheme.colorScheme.error
                )
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun TextFieldDisabledPreview() {
    var value by remember { mutableStateOf(TextFieldValue("Disabled field")) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = "Disabled Field",
            placeholder = {
                Text(
                    "This field is disabled",
                    style = NovixTheme.typography.body.small
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_user),
            enabled = false,
            interactionSource = interactionSource,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun TextFieldNoLabelPreview() {
    var value by remember { mutableStateOf(TextFieldValue("No label example")) }
    val interactionSource = remember { MutableInteractionSource() }

    NovixTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            placeholder = {
                Text(
                    "Field without label",
                    style = NovixTheme.typography.body.small
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_user),
            interactionSource = interactionSource,
            modifier = Modifier.padding(16.dp)
        )
    }
}
