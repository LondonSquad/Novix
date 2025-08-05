package com.london.presentation.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.imageharamblur.ui.ImageFilterConfig
import com.london.imageharamblur.ui.ImageViewFilter

@Composable
fun ImageView(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentRestrictionLevel: ContentRestrictionLevel? = null,
    onLoadingStateChange: ((Boolean) -> Unit)? = null,
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (String?) -> Unit = {},
    moderatedContent: @Composable () -> Unit = {}
) {
    val effectiveRestrictionLevel = contentRestrictionLevel ?: LocalContentRestrictionLevel.current

    val config = remember(effectiveRestrictionLevel) {
        when (effectiveRestrictionLevel) {
            ContentRestrictionLevel.STRICT -> ImageFilterConfig(
                enableModeration = true,
                detectFemales = true,
                useContentDetection = true,
            )

            ContentRestrictionLevel.MODERATE -> ImageFilterConfig(
                enableModeration = true,
                detectFemales = false,
                detectMales = false,
                useContentDetection = true,
            )

            ContentRestrictionLevel.OFF -> ImageFilterConfig(
                enableModeration = false
            )
        }
    }

    ImageViewFilter(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        config = config,
        onLoadingStateChange = onLoadingStateChange,
        loadingContent = loadingContent,
        errorContent = errorContent,
        moderatedContent = moderatedContent
    )
}