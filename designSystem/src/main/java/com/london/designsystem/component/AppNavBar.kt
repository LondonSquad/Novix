package com.london.designsystem.component

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
            .topBorder(borderColor, 1.dp)
            .background(color = backgroundColor)
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navDestinations.forEach { item ->
            val isSelected = currentSelectedRoute == item.route

            NavBarItem(
                item = item,
                isSelected = isSelected,
                selectedIconColor = selectedIconColor,
                idleIconColor = idleIconColor,
                onClick = {
                    if (!isSelected) {
                        currentSelectedRoute = item.route
                        onNavDestinationClicked(item.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavigationTab,
    isSelected: Boolean,
    selectedIconColor: Color,
    idleIconColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(width = 60.dp, height = 56.dp),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = isSelected,
            enter = slideInVertically(
                animationSpec = tween(400, easing = FastOutSlowInEasing),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                animationSpec = tween(300, easing = FastOutLinearInEasing),
                targetOffsetY = { it }
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Icon(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .align(Alignment.BottomCenter)
                        .blur(radius = 54.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                    painter = R.drawable.ellipse_blur_filled.painter,
                    contentDescription = null,
                    tint = selectedIconColor
                )
            } else {
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .scale(2f),
                    painter = R.drawable.ellipse_19.painter,
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            contentAlignment = Alignment.Center
        ) {

            Crossfade(
                targetState = isSelected,
                animationSpec = tween(300),
                label = "iconCrossfade"
            ) { selected ->
                Icon(
                    painter = if (selected) item.selectedIcon else item.idleIcon,
                    contentDescription = item.route,
                    modifier = Modifier
                        .size(24.dp)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    tint = if (selected) selectedIconColor else idleIconColor
                )
            }

            AnimatedVisibility(
                modifier = Modifier
                    .offset(y = 2.dp)
                    .align(Alignment.BottomCenter),
                visible = isSelected,
                enter = slideInVertically(
                    animationSpec = tween(450, easing = FastOutSlowInEasing),
                    initialOffsetY = { it * 2 }
                ) + fadeIn(),
                exit = slideOutVertically(
                    animationSpec = tween(250, easing = FastOutLinearInEasing),
                    targetOffsetY = { it * 2 }
                ) + fadeOut()
            ) {
                Icon(
                    modifier = Modifier
                        .size(4.dp)
                        .align(Alignment.BottomCenter),
                    painter = R.drawable.ellipse_selected_dot.painter,
                    contentDescription = null,
                    tint = selectedIconColor
                )
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
            currentRoute = "search",
            onNavDestinationClicked = {}
        )
    }
}