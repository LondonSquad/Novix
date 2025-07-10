package com.london.app.navigation

import androidx.compose.runtime.Composable
import com.london.designsystem.R
import com.london.designsystem.component.NavigationTab
import com.london.designsystem.utils.painter

class NavigationHelper {
    companion object {
        @Composable
        fun getNavigationTabs(): List<NavigationTab> {
            return listOf(
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
            )
        }
    }
}