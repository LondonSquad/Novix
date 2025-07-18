package com.london.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme

@Composable
fun UnSuitableEye(
    modifier: Modifier = Modifier,
    isSmallPicture: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_eye),
            contentDescription = stringResource(R.string.unsuitable_eye_icon),
            tint = NovixTheme.colors.body
        )
        if (!isSmallPicture)
            Text(
                text = stringResource(R.string.unsuitable_eye_content),
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.body
            )
    }
}