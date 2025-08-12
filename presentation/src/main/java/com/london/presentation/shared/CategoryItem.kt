package com.london.presentation.shared

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.Text
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.theme.noRippleClickable


@Composable
fun CategoriesItem(
    categoryName: String,
    categoryImage: Any?,
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
        ImageView(
            model = categoryImage,
            errorContent = { ErrorImage(NovixTheme.isThemeDark) },
            contentDescription = "Image of $categoryName",
            modifier = Modifier.fillMaxSize(),
            contentScale = Crop,
            contentRestrictionLevel = null,
            loadingContent = { CircularLoading(modifier = Modifier.align(Alignment.Center)) },
            moderatedContent = { UnSuitableEye() }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NovixTheme.colors.linearGradient,
                            NovixTheme.colors.linearGradient.copy(alpha = 0.8f),
                            NovixTheme.colors.linearGradient.copy(alpha = 0.7f),
                            NovixTheme.colors.linearGradient.copy(alpha = 0.0f),
                        ),
                        startX = 0f,
                    )
                )
        )
        Text(
            text = categoryName,
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.onPrimary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp, end = 52.dp, top = 8.dp)
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
            categoryName = "Adventure",
            categoryImage = ""
        ),
        CategoryItem(
            categoryName = "Drama",
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
    val categoryName: String,
    val categoryImage: String,
)