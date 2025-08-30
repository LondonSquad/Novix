package com.london.app.navigation

import androidx.compose.runtime.Composable
import com.london.designsystem.R
import com.london.designsystem.component.NavigationTab
import com.london.designsystem.component.TabIcons
import com.london.designsystem.utils.painter
import com.london.presentation.navigation.Screen

class NavigationHelper {
    companion object {
        @Composable
        fun getNavigationTabs(): List<NavigationTab<Screen>> {
            return listOf(
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_home.painter,
                        selectedIcon = R.drawable.icon_home_filled.painter
                    ),
                    destination = Screen.Home,
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_search.painter,
                        selectedIcon = R.drawable.icon_search_filled.painter
                    ),
                    destination = Screen.Search,
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_masks.painter,
                        selectedIcon = R.drawable.icon_masks_filled.painter,
                    ),

                    destination = Screen.Categories,
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_bookmark.painter,
                        selectedIcon = R.drawable.icon_bookmark_filled.painter,
                    ),
                    destination = Screen.Lists(),
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_user.painter,
                        selectedIcon = R.drawable.icon_user_filled.painter
                    ),
                    destination = Screen.Account,
                )
            )
        }
    }
}
