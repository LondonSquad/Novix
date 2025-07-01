package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.topBorder

data class NavigationTab(
    val idleIcon: Painter,
    val selectedIcon: Painter,
    val route: String
)

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navDestinations: List<NavigationTab>,
    currentRoute: String,
    onNavDestinationClicked: (String) -> Unit,
    backgroundColor: Color = NovixTheme.colors.surface,
    selectedIconColor: Color = NovixTheme.colors.primary,
    idleIconColor: Color = NovixTheme.colors.hint,
    borderColor: Color = NovixTheme.colors.stroke
) {
    var currentSelectedRoute by remember { mutableStateOf(currentRoute) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .topBorder(borderColor, 1.dp)
            .padding(/*horizontal = 32.dp, */vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navDestinations.forEach { item ->
            val isSelected = currentSelectedRoute == item.route

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        if (isSelected.not()) {
                            currentSelectedRoute = item.route
                            onNavDestinationClicked(item.route)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isSelected) item.selectedIcon else item.idleIcon,
                    contentDescription = item.route,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) selectedIconColor else idleIconColor
                )

                if (isSelected) {
                    Icon(
                        modifier = Modifier
                            .offset(y = 1.dp)
                            .size(4.dp)
                            .align(Alignment.BottomCenter),
                        painter = R.drawable.ellipse_selected_dot.painter,
                        tint = selectedIconColor,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun NavBarPreview() {
    NovixTheme {
        NavBar(
            navDestinations = listOf(
                NavigationTab(
                    idleIcon = R.drawable.icon_home.painter,
                    selectedIcon = R.drawable.icon_home_filled.painter,
                    route = "home"
                ),
                NavigationTab(
                    idleIcon = R.drawable.icon_search.painter,
                    selectedIcon = R.drawable.icon_search_filled.painter,
                    route = "search"
                ),
                NavigationTab(
                    idleIcon = R.drawable.icon_masks.painter,
                    selectedIcon = R.drawable.icon_masks_filled.painter,
                    route = "categories"
                ),
                NavigationTab(
                    idleIcon = R.drawable.icon_bookmark.painter,
                    selectedIcon = R.drawable.icon_bookmark_filled.painter,
                    route = "bookmarks"
                ),
                NavigationTab(
                    idleIcon = R.drawable.icon_user.painter,
                    selectedIcon = R.drawable.icon_user_filled.painter,
                    route = "account"
                )
            ),
            currentRoute = "home",
            onNavDestinationClicked = {}
        )
    }
}