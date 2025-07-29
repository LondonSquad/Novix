package com.london.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.london.imageharamblur.ui.ImageViewFilter

@Composable
fun ImageView(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    onLoadingStateChange: ((Boolean) -> Unit)? = null,
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (String?) -> Unit = {},
    moderatedContent: @Composable () -> Unit = {}
) {
    ImageViewFilter(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        onLoadingStateChange = onLoadingStateChange,
        loadingContent = loadingContent,
        errorContent = errorContent,
        moderatedContent = moderatedContent
    )
}