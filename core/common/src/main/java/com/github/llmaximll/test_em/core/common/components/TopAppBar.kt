package com.github.llmaximll.test_em.core.common.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.llmaximll.test_em.core.common.R
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    @StringRes titleRes: Int?
) {
    CenterAlignedTopAppBar(
        title = {
            if (titleRes != null) {
                Text(
                    text = stringResource(id = titleRes),
                    style = CustomTypography.title1
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
private fun CommonTopAppBarPreview() {
    CommonTopAppBar(titleRes = R.string.core_common_placeholder)
}