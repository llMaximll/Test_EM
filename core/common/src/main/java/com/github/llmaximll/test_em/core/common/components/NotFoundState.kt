package com.github.llmaximll.test_em.core.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

@Composable
fun NotFoundState(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = textRes),
                color = AppColors.TextBlack,
                style = CustomTypography.title2
            )
        }
    }
}