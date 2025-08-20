package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme

@Composable
fun GuestUserLoginBottomSheet(
    onDismissClick: () -> Unit,
    onLoginClick: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismissClick,
        state = sheetState,
        containerColor = NovixTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovixTheme.colors.surface)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally

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
                    onClick = onDismissClick,
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = NovixTheme.colors.iconBackgroundLow
                        )
                        .size(32.dp)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.close),
                        tint = NovixTheme.colors.body,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.user_person_profile),
                contentDescription = stringResource(R.string.user_person_profile),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(100.dp)
            )
            Text(
                stringResource(R.string.please_login_to_rate_your_favorite_items),
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body
            )
            PrimaryButton(
                stringResource(R.string.login),
                hasLabel = true,
                icon = R.drawable.icondesign,
                hasIcon = false,
                isLoading = false,
                onClick = onLoginClick,
                enabled = true,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
