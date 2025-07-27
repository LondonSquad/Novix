package com.london.designsystem.component

import androidx.annotation.StringRes
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun TabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, tab ->
            NovixTab(
                text = tab.text,
                isSelected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                modifier = Modifier.weight(1f)
            )
        }
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

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(text),
            style = NovixTheme.typography.label.medium,
            color = if (isSelected) NovixTheme.colors.title else NovixTheme.colors.hint
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(NovixTheme.colors.primary)

            )
        } else
            Spacer(modifier = Modifier.height(8.dp))

    }
}

@Immutable
data class TabItem(
    @StringRes val
    text: Int,
)


@ThemePreviews
@Composable
private fun NovixTabLayoutWithPagerPreview() {
    NovixTheme {
        TabLayout(
            tabs = listOf(
                TabItem(R.string.movies),
                TabItem(R.string.tv_shows),
            ),
            selectedIndex = 0,
            onTabSelected = {}
        )
    }
}