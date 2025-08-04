package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme

@Composable
fun RatingBottomSheet(
    onDismissRequest: () -> Unit,
    onSubmitClick: (Int) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val sheetState = remember {
        SheetState(
            androidx.compose.material3.SheetState(
                initialValue = SheetValue.Expanded,
                density = density,
                confirmValueChange = { true },
                skipPartiallyExpanded = false
            )
        )
    }
    NovixTheme {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            state = sheetState,
            containerColor = NovixTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NovixTheme.colors.surface)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.rate_it),
                        color = NovixTheme.colors.title,
                        style = NovixTheme.typography.title.large
                    )
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.background(
                           shape = RoundedCornerShape(8.dp),
                           color =  NovixTheme.colors.iconBackgroundLow
                        ).size(32.dp)

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cancel_01),
                            contentDescription = stringResource(R.string.close),
                            tint = NovixTheme.colors.body,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.select_how_much_you_like_it),
                    color = NovixTheme.colors.body,
                    style = NovixTheme.typography.body.medium,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { rating = i },
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (i <= rating) R.drawable.ic_star_filled
                                    else R.drawable.ic_star_outline
                                ),
                                contentDescription = if (i <= rating) stringResource(R.string.filled_star) else stringResource(
                                    R.string.outline_star
                                ),
                                tint = NovixTheme.colors.yellowAccent,
                                modifier = Modifier
                            )
                        }
                    }
                }

                PrimaryButton(
                    onClick = { onSubmitClick(rating) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = rating > 0,
                    text = stringResource(R.string.submit),
                    hasLabel = true,
                    icon = R.drawable.ic_star_outline,
                    hasIcon = false,
                    isLoading = false
                )
            }
        }
    }
}
