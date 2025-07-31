package com.london.presentation.feature.details.actordetails.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.imageharamblur.ui.ImageViewFilter
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun ActorGalleryScreen(
    onNavigateBack: () -> Unit,
    viewModel: ActorGalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect.Listen<ActorGalleryEffectUiState> { onNavigateBack() }

    BuildScreen(
        onBack = viewModel::onBackClick,
        isLoading = uiState.isLoading,
        isError = uiState.error != null
    ) {
        Content(
            actorGalleryContract = viewModel,
            uiState = uiState
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    actorGalleryContract: ActorGalleryContract,
    uiState: ActorGalleryUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        TopBar(
            modifier = Modifier.padding(bottom = 16.dp),
            title = stringResource(R.string.gallery),
            onBackClick = actorGalleryContract::onBackClick
        )

        Box(modifier = Modifier.weight(1f)) {
            if (uiState.isLoading) {
                CircularLoading(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 104.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
                                .border(
                                    width = 1.dp,
                                    color = NovixTheme.colors.stroke,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp)),
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

