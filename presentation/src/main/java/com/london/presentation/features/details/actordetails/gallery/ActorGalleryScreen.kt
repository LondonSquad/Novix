package com.london.presentation.features.details.actordetails.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorGalleryScreen(
    onBackClick: () -> Unit,
    viewModel: ActorGalleryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        TopBar(
            modifier = Modifier.padding(bottom = 5.dp),
            title = stringResource(R.string.gallery),
            onBackClick = onBackClick
        )
        Box(modifier = Modifier.weight(1f)) {
            if (uiState.isLoading) {
                CircularLoading(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 104.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NovixTheme.colors.surface)
                ) {
                    items(uiState.images) { imageUrl ->
                        ImageViewFilter(
                            model = imageUrl,
                            contentDescription = stringResource(R.string.actor_photos),
                            modifier = Modifier
                                .size(width = 104.dp, height = 101.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                            loadingContent = { CircularLoading() },
                            errorContent = { ErrorImage() }
                        )
                    }
                }
            }
        }
    }
}

