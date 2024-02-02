package com.github.llmaximll.test_em.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

const val routeMainScreen = "main"

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.placeholder),
            style = CustomTypography.title1,
            color = AppColors.TextDarkGrey,
            textAlign = TextAlign.Center
        )
    }
}