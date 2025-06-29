package com.london.novix.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.london.novix.R
import com.london.novix.presentation.designSystem.theme.NovixTheme
import com.london.novix.presentation.designSystem.theme.ThemePreviews


@Composable
fun CategoriesItem(
    text: String,
    image: Any,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .height(68.dp)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = image,
            placeholder = painterResource(R.drawable.frame1597883073),
            error = painterResource(R.drawable.frame1597883073),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = Crop
        )
        Text(
            text = text,
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.onPrimary,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
        )


    }
}


@Composable
fun CategoryGrid(
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier
            .background(NovixTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoriesItem(
                text = category,
                image = R.drawable.frame1597883073,
                onClick = { },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun CategoryGridPreview() {
    NovixTheme {
        val categories = listOf("Documentary", "Adventure", "Action", "Comedy", "Drama", "Horror")
        Column(
            modifier = Modifier.background(NovixTheme.colors.surface),
        ) {
            CategoryGrid(
                categories = categories,
            )
        }
    }
}