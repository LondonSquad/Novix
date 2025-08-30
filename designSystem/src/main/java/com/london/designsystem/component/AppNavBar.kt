package com.london.designsystem.component

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.topBorder

data class NavigationTab<out T>(
    val tabIcons: TabIcons,
    val destination: T,
)

data class TabIcons(
    val idleIcon: Painter,
    val selectedIcon: Painter
)

data class NavBarColors(
    val backgroundColor: Color,
    val selectedIconColor: Color,
    val idleIconColor: Color,
    val topBorderColor: Color
)

object NavBarDefaults {

    @Composable
    fun navBarColors() = NavBarColors(
        backgroundColor = NovixTheme.colors.surface,
        selectedIconColor = NovixTheme.colors.primary,
        idleIconColor = NovixTheme.colors.hint,
        topBorderColor = NovixTheme.colors.stroke
    )
}

private object NavBarDimens {
    val itemWidth = 60.dp
    val itemHeight = 56.dp
    val iconSize = 24.dp
    val clickableAreaSize = 42.dp
    val dotSize = 4.dp
    val verticalPadding = 7.dp
    val dotPaddingFromBottom = 12.dp
    val borderWidth = 1.dp
}

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navDestinations: List<NavigationTab<Any>>,
    onNavDestinationClicked: (Any) -> Unit,
    navBackStackEntry: NavBackStackEntry? = null,
    navBarColors: NavBarColors = NavBarDefaults.navBarColors()
) {

    val checkIfSelected: (Any) -> Boolean = { destination ->
        navBackStackEntry?.destination?.hasRoute(destination::class) == true
    }

    val selectedIndex =
        navDestinations.indexOfFirst { item -> checkIfSelected(item.destination) }.takeIf { it >= 0 } ?: 0

    Box(
        modifier = modifier
            .background(color = navBarColors.backgroundColor)
            .navigationBarsPadding()
            .fillMaxWidth()
            .topBorder(color = navBarColors.topBorderColor, width = NavBarDimens.borderWidth)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = NavBarDimens.verticalPadding),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navDestinations.forEachIndexed { index, item ->
                NavBarItem(
                    tabIcons = item.tabIcons,
                    selectedIconColor = navBarColors.selectedIconColor,
                    idleIconColor = navBarColors.idleIconColor,
                    onClick = { onNavDestinationClicked(item.destination) },
                    isSelected = checkIfSelected(item.destination)
                )
            }
        }

        MovingDotIndicator(
            selectedIndex = selectedIndex,
            itemCount = navDestinations.size,
            selectedIconColor = navBarColors.selectedIconColor,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun MovingDotIndicator(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    itemCount: Int,
    selectedIconColor: Color
) {
    val screenWidth = LocalDensity.current.run {
        androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
    }

    val totalItemsWidth = NavBarDimens.itemWidth * itemCount
    val remainingSpace = screenWidth - totalItemsWidth
    val spaceBetweenItems = if (itemCount > 1) remainingSpace / (itemCount + 1) else 0.dp

    val itemCenterOffset = spaceBetweenItems + (NavBarDimens.itemWidth / 2)
    val spacingBetweenCenters = NavBarDimens.itemWidth + spaceBetweenItems

    val offsetX by animateDpAsState(
        targetValue = itemCenterOffset + (spacingBetweenCenters * selectedIndex) - (NavBarDimens.dotSize / 2),
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "dotOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = NavBarDimens.dotPaddingFromBottom)
    ) {
        Icon(
            modifier = Modifier
                .size(NavBarDimens.dotSize)
                .offset(x = offsetX),
            painter = painterResource(R.drawable.ellipse_selected_dot),
            contentDescription = null,
            tint = selectedIconColor
        )
    }
}

@Composable
private fun NavBarItem(
    tabIcons: TabIcons,
    isSelected: Boolean,
    selectedIconColor: Color,
    idleIconColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(width = NavBarDimens.itemWidth, height = NavBarDimens.itemHeight),
        contentAlignment = Alignment.Center
    ) {
        AnimatedBackgroundBlur(
            modifier = Modifier.align(Alignment.BottomCenter),
            isVisible = isSelected,
            selectedIconColor = selectedIconColor
        )

        ClickableIconContainer(
            tabIcons = tabIcons,
            isSelected = isSelected,
            selectedIconColor = selectedIconColor,
            idleIconColor = idleIconColor,
            onClick = onClick
        )
    }
}

@Composable
private fun AnimatedBackgroundBlur(
    isVisible: Boolean,
    selectedIconColor: Color,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = scaleIn(animationSpec = tween(400, easing = FastOutSlowInEasing)),
        exit = scaleOut(animationSpec = tween(300, easing = FastOutLinearInEasing))
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Icon(
                modifier = Modifier
                    .width(NavBarDimens.itemWidth)
                    .height(16.dp)
                    .blur(radius = 54.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                painter = painterResource(R.drawable.ellipse_blur_filled),
                contentDescription = null,
                tint = selectedIconColor
            )
        } else {
            Image(
                modifier = Modifier.scale(2f),
                painter = painterResource(R.drawable.ellipse_pre_blurred),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ClickableIconContainer(
    tabIcons: TabIcons,
    isSelected: Boolean,
    selectedIconColor: Color,
    idleIconColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(NavBarDimens.clickableAreaSize)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedNavIcon(
            tabIcons = tabIcons,
            isSelected = isSelected,
            selectedIconColor = selectedIconColor,
            idleIconColor = idleIconColor
        )
    }
}

@Composable
private fun AnimatedNavIcon(
    tabIcons: TabIcons,
    isSelected: Boolean,
    selectedIconColor: Color,
    idleIconColor: Color
) {
    Crossfade(
        targetState = isSelected,
        animationSpec = tween(300),
        label = "iconCrossfade"
    ) { selected ->
        Icon(
            painter = if (selected) tabIcons.selectedIcon else tabIcons.idleIcon,
            modifier = Modifier
                .size(NavBarDimens.iconSize)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            contentDescription = null,
            tint = if (selected) selectedIconColor else idleIconColor
        )
    }
}

@ThemePreviews
@Composable
private fun NavBarPreview() {
    data class MockDestination(val name: String)

    var currentSelectedDestination by remember { mutableStateOf(MockDestination("home")) }

    NovixTheme {
        NavBar(
            navDestinations = listOf(
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_home.painter,
                        selectedIcon = R.drawable.icon_home_filled.painter
                    ),
                    destination = MockDestination("home"),
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_search.painter,
                        selectedIcon = R.drawable.icon_search_filled.painter
                    ),
                    destination = MockDestination("search"),
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_masks.painter,
                        selectedIcon = R.drawable.icon_masks_filled.painter,
                    ),

                    destination = MockDestination("categories"),
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_bookmark.painter,
                        selectedIcon = R.drawable.icon_bookmark_filled.painter,
                    ),
                    destination = MockDestination("bookmarks"),
                ),
                NavigationTab(
                    TabIcons(
                        idleIcon = R.drawable.icon_user.painter,
                        selectedIcon = R.drawable.icon_user_filled.painter
                    ),
                    destination = MockDestination("account"),
                )
            ),
            onNavDestinationClicked = { destination ->
                if (destination != currentSelectedDestination) {
                    currentSelectedDestination
                }
            },
        )
    }
}
