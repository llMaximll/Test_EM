package com.github.llmaximll.test_em.core.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.llmaximll.test_em.core.common.R
import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

@Composable
fun CommonItemsList(
    items: List<Item>,
    favoriteItemIds: List<String>,
    onMarkFavorite: (String, Boolean) -> Unit,
    onProductDetailsNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val row by remember { derivedStateOf { lazyGridState.layoutInfo.visibleItemsInfo.find { it.index == index }?.row } }
            val itemsInRow by remember { derivedStateOf { lazyGridState.layoutInfo.visibleItemsInfo.filter { it.row == row } } }
            val maxHeightInRow = itemsInRow.maxOfOrNull { it.size.height }
            val maxHeightInRowDp = with(density) { maxHeightInRow?.toDp() } ?: Dp.Unspecified

            CommonItem(
                modifier = Modifier
                    .height(maxHeightInRowDp),
                item = item,
                isFavorite = item.id in favoriteItemIds,
                onMarkFavorite = onMarkFavorite,
                onProductDetailsNavigate = onProductDetailsNavigate
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CommonItem(
    item: Item,
    isFavorite: Boolean,
    onMarkFavorite: (String, Boolean) -> Unit,
    onProductDetailsNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onProductDetailsNavigate(item.id) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = AppColors.BackgroundLightGrey
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = AppColors.BackgroundWhite
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val drawableResList =
                com.github.llmaximll.test_em.core.common.Image.entries.filter { item.id in it.ids }
                    .map { it.drawableRes }

            Box(
                contentAlignment = Alignment.Center
            ) {
                val pagerState = rememberPagerState { drawableResList.size }
                HorizontalPager(
                    state = pagerState
                ) { index ->
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(144.dp),
                        painter = painterResource(
                            id = drawableResList[index]
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    onClick = { onMarkFavorite(item.id, !isFavorite) }
                ) {
                    AnimatedContent(
                        targetState = isFavorite,
                        label = "FavoriteIcon"
                    ) { value ->
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = if (value) R.drawable.ic_heart_active else R.drawable.ic_heart),
                            contentDescription = null,
                            tint = AppColors.ElementPink
                        )
                    }
                }

                CommonPagerTabs(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    pagerState = pagerState
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "${item.price?.price} ${item.price?.unit}",
                    style = CustomTypography.text1.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = AppColors.TextGrey
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${item.price?.priceWithDiscount} ${item.price?.unit}",
                        style = CustomTypography.title2,
                        color = AppColors.TextBlack
                    )

                    DiscountElement(discount = item.price?.discount ?: 0)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    style = CustomTypography.title3,
                    color = AppColors.TextBlack
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.subtitle,
                    style = CustomTypography.caption1,
                    color = AppColors.TextDarkGrey
                )

                val feedback = item.feedback
                if (feedback != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_small_star),
                            contentDescription = null,
                            tint = AppColors.ElementOrange
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = feedback.rating.toString(),
                            style = CustomTypography.text1,
                            color = AppColors.TextOrange
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = "(${feedback.count})",
                            style = CustomTypography.text1,
                            color = AppColors.TextGrey
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(
                        color = AppColors.ElementPink,
                        shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                    ),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    tint = AppColors.ElementWhite
                )
            }
        }
    }
}