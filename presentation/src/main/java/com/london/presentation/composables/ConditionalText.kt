package com.london.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme

@Composable
fun ConditionalText(
    text: String,
    expandedState: Boolean,
    modifier: Modifier = Modifier,
    minimumLineLength: Int = 4,
    onExpandedChange: () -> Unit
) {
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    var truncatedText by remember { mutableStateOf("") }

    val textStyle = NovixTheme.typography.body.small.copy(color = NovixTheme.colors.body)
    val actionStyle = SpanStyle(
        color = NovixTheme.colors.primary,
        fontSize = NovixTheme.typography.label.medium.fontSize,
        fontWeight = NovixTheme.typography.label.medium.fontWeight
    )

    Text(
        text = buildAnnotatedString {
            when {
                !showReadMoreButtonState -> append(text)
                !expandedState -> {
                    append(truncatedText)
                    append("  ")
                    withStyle(actionStyle) { append(stringResource(R.string.read_more)) }
                }
                else -> {
                    append(text)
                    append("  ")
                    withStyle(actionStyle) { append(stringResource(R.string.read_less)) }
                }
            }
        },
        style = textStyle,
        maxLines = if (expandedState || !showReadMoreButtonState) Int.MAX_VALUE else minimumLineLength,
        onTextLayout = { textLayoutResult ->
            if (!showReadMoreButtonState && textLayoutResult.lineCount > minimumLineLength) {
                val lastVisibleLineEndIndex = textLayoutResult.getLineEnd(minimumLineLength - 1)
                val readMoreLength = 15
                var truncateIndex = maxOf(0, lastVisibleLineEndIndex - readMoreLength)

                while (truncateIndex > 0 && text[truncateIndex] != ' ') {
                    truncateIndex--
                }

                truncatedText = text.substring(0, truncateIndex).trim()
                showReadMoreButtonState = true
            }
        },
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (showReadMoreButtonState) onExpandedChange()
            }
    )
}