package com.github.llmaximll.test_em.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

@Composable
fun DiscountElement(
    discount: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .background(
                color = AppColors.BackgroundPink,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = "-$discount%",
        style = CustomTypography.text1,
        color = AppColors.TextWhite
    )
}