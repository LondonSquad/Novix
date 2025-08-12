package com.london.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

interface Tabbable {
    val tabTextResId: Int
}

@Composable
fun <T : Tabbable> TabLayout(
    tabs: List<T>,
    selectedTab: T?,
    onTabSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEach { tab ->
                NovixTab(
                    text = tab.tabTextResId,
                    isSelected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(NovixTheme.colors.stroke)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun NovixTab(
    @StringRes text: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textColor by animateColorAsState(
        targetValue = if (isSelected) NovixTheme.colors.title else NovixTheme.colors.hint,
        animationSpec = tween(
            durationMillis = 300,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "text_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = tween(
            durationMillis = 350,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "scale"
    )

    val indicatorWidth by animateFloatAsState(
        targetValue = if (isSelected) 0.7f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "indicator_width"
    )

    val indicatorAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "indicator_alpha"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(text),
            style = NovixTheme.typography.label.medium,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(indicatorWidth)
                .height(3.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(NovixTheme.colors.primary.copy(alpha = indicatorAlpha))
        )
    }
}

@ThemePreviews
@Composable
private fun NovixTabLayoutWithPagerPreview() {

    data class TabbableItem(override val tabTextResId: Int) : Tabbable

    NovixTheme {
        TabLayout(
            tabs = listOf(
                TabbableItem(R.string.movies),
                TabbableItem(R.string.tv_shows)
            ),
            selectedTab = TabbableItem(R.string.movies),
            onTabSelected = { },
        )
    }
}