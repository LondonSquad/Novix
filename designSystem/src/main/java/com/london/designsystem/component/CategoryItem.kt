package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.horizontalGradient
import com.london.designsystem.theme.noRippleClickable


@Composable
fun CategoriesItem(
    categoryName: List<String>,
    categoryImage: String,
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
            .noRippleClickable(onClick = onClick),
    ) {
        AsyncImage(
            model = categoryImage,
            placeholder = painterResource(R.drawable.frame1597883073),
            error = painterResource(R.drawable.frame1597883073),
            contentDescription = "Image of ${categoryName.joinToString()}",
            modifier = Modifier.fillMaxSize(),
            contentScale = Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.horizontalGradient(horizontalGradient))
        )
        Text(
            text = categoryName.joinToString(separator = " &\n"),
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.onPrimary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CategoryGrid(
    categories: List<CategoryItem>,
    onCategoryItemClick: (CategoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.background(NovixTheme.colors.surface),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoriesItem(
                categoryName = category.categoryName,
                categoryImage = category.categoryImage,
                onClick = { onCategoryItemClick(category) },
            )
        }
    }
}

@ThemePreviews
@Composable
fun CategoryGridPreview() {

    val categories = listOf(
        CategoryItem(
            categoryName = listOf("Action", "Adventure"),
            categoryImage = ""
        ),
        CategoryItem(
            categoryName = listOf("Drama"),
            categoryImage = ""
        ),

        )
    NovixTheme {
        Column(
            modifier = Modifier
                .background(NovixTheme.colors.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            CategoryGrid(
                categories = categories,
                onCategoryItemClick = {
                    // Handle category item click
                }
            )
        }
    }
}

// Fake Data class to represent a category item
data class CategoryItem(
    val categoryName: List<String>,
    val categoryImage: String,
)