package com.london.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme

@Composable
fun ReadMoreText(
    modifier: Modifier = Modifier,
    content: String,
    maxLines: Int = 4
) {
    var maxLines by rememberSaveable { mutableIntStateOf(maxLines) }
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text(
            text = content,
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (isTextCollapsed)
                stringResource(R.string.read_less) else stringResource(R.string.read_more),
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.primary,
            modifier = Modifier
                .clickable {
                    maxLines = if (maxLines == 4) Int.MAX_VALUE else 4
                    isTextCollapsed = !isTextCollapsed
                }
        )
    }
}