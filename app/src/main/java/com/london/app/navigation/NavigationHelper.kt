package com.london.app.navigation

import com.london.designsystem.R
import com.london.designsystem.component.NavigationTab
import com.london.presentation.navigation.Screen

object NavigationHelper {
    val destinations = arrayOf(
        NavigationTab(
            unselectedIcon = R.drawable.icon_home,
            selectedIcon = R.drawable.icon_home_filled,
            destination = Screen.Home,
        ),
        NavigationTab(
            unselectedIcon = R.drawable.icon_search,
            selectedIcon = R.drawable.icon_search_filled,
            destination = Screen.Search,
        ),
        NavigationTab(
            unselectedIcon = R.drawable.icon_masks,
            selectedIcon = R.drawable.icon_masks_filled,
            destination = Screen.Categories,
        ),
        NavigationTab(
            unselectedIcon = R.drawable.icon_bookmark,
            selectedIcon = R.drawable.icon_bookmark_filled,
            destination = Screen.Lists(),
        ),
        NavigationTab(
            unselectedIcon = R.drawable.icon_user,
            selectedIcon = R.drawable.icon_user_filled,
            destination = Screen.Account,
        )
    )
}
