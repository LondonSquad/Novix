package com.london.presentation.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun HomeCard(
    imageUrl: Any,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    hasSaveIcon: Boolean = true,
    imageDescription: String? = null,
    onSaveClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        ImageView(
            model = imageUrl,
            contentDescription = imageDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            errorContent = { ErrorImage() },
            loadingContent = { CircularLoading(modifier = Modifier.align(Alignment.Center)) },
            moderatedContent = { UnSuitableEye() }
        )
        if (hasSaveIcon)
            SaveIcon(
                isSaved = isSaved,
                onSaveClick = onSaveClick,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                iconTint = NovixTheme.colors.onPrimary,
            )
    }
}

@ThemePreviews
@Composable
fun HomeCardPreview() {
    NovixTheme {
        HomeCard(
            imageUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            isSaved = false,
            onSaveClick = {}
        )
    }
}