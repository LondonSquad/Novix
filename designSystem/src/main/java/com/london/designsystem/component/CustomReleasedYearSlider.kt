package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomReleasedYearSlider(
    yearRange: ClosedFloatingPointRange<Float>,
    onYearRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    minYear: Int = 1995,
    maxYear: Int = 2025
) {

    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = minYear.toString(),
                color = NovixTheme.colors.body,
                style = NovixTheme.typography.label.small
            )
            Text(
                text = maxYear.toString(),
                color = NovixTheme.colors.body,
                style = NovixTheme.typography.label.small
            )
        }

        RangeSlider(
            value = yearRange,
            onValueChange = { onYearRangeChange(it) },
            valueRange = minYear.toFloat()..maxYear.toFloat(),
            track = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(100))
                        .background(NovixTheme.colors.surface)
                        .border(
                            width = 1.dp,
                            color = NovixTheme.colors.stroke,
                            shape = RoundedCornerShape(100)
                        )
                )
                val startFraction = (yearRange.start - minYear) / (maxYear - minYear)
                val endFraction = (yearRange.endInclusive - minYear) / (maxYear - minYear)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = endFraction)
                            .padding(start = with(LocalDensity.current) {
                                (startFraction * (LocalConfiguration.current.screenWidthDp - 32).dp.toPx()).toDp()
                            })
                            .height(8.dp)
                            .background(Color(0xFFF2674A))
                    )
                }
            },
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFFF2674A), shape = CircleShape)
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFFF2674A), shape = CircleShape)
                )
            }
        )
    }
}

