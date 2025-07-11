package com.london.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.london.designsystem.R
import com.london.designsystem.component.NavigationTab
import com.london.designsystem.utils.painter

class NavigationHelper {
    companion object {
        @Composable
        fun getNavigationTabs(

        ): List<NavigationTab> {
            return listOf(
                NavigationTab(
                    idleIcon = painterResource(R.drawable.icon_home),
                    selectedIcon = painterResource(R.drawable.icon_home_filled),
                    route = "home"
                ),
                NavigationTab(
                    idleIcon = painterResource(R.drawable.icon_search),
                    selectedIcon = painterResource(R.drawable.icon_search_filled),
                    route = "search"
                ),
                NavigationTab(
                    idleIcon = painterResource(R.drawable.icon_masks),
                    selectedIcon = painterResource(R.drawable.icon_masks_filled),
                    route = "categories"
                ),
                NavigationTab(
                    idleIcon = painterResource(R.drawable.icon_bookmark),
                    selectedIcon = painterResource(R.drawable.icon_bookmark_filled),
                    route = "bookmarks"
                ),
                NavigationTab(
                    idleIcon = painterResource(R.drawable.icon_user),
                    selectedIcon = painterResource(R.drawable.icon_user_filled),
                    route = "account"
                )
            )
        }
    }
}