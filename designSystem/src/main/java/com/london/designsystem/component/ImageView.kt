package com.london.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.R

@Composable
fun ImageView(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    onLoadingStateChange: ((Boolean) -> Unit)? = null,
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (String?) -> Unit = {}

) {
    ImageViewFilter(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        onLoadingStateChange = onLoadingStateChange,
        loadingContent = loadingContent,
        errorContent = errorContent ,
    )
}