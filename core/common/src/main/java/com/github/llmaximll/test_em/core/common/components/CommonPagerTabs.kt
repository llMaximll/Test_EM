package com.github.llmaximll.test_em.core.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.llmaximll.test_em.core.common.theme.AppColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonPagerTabs(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = pagerState.currentPage,
        label = "ContentItemImageTabs"
    ) { currentIndex ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pagerState.pageCount) { index ->
                Spacer(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (index == currentIndex) AppColors.ElementPink else AppColors.ElementLightGrey,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}