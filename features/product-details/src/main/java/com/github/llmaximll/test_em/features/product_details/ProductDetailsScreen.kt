package com.github.llmaximll.test_em.features.product_details

import android.content.res.Resources
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.Image
import com.github.llmaximll.test_em.core.common.components.CommonPagerTabs
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import com.smarttoolfactory.ratingbar.RatingBar
import com.smarttoolfactory.ratingbar.model.GestureStrategy
import kotlinx.coroutines.yield
import com.github.llmaximll.test_em.core.common.R as ResCommon

const val routeProductDetailsScreen = "product_details"

@Composable
fun ProductDetailsScreen(
    productId: String?,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val itemState by viewModel.itemState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState(false)

    LaunchedEffect(itemState) {
        log(
            """ProductId: $productId 
            |ItemState: $itemState""".trimMargin()
        )
    }

    ProductDetailsState(
        modifier = modifier,
        itemState = itemState,
        isFavorite = isFavorite,
        onMarkFavorite = viewModel::markItemFavorite
    )
}

@Composable
fun ProductDetailsState(
    itemState: ItemState,
    isFavorite: Boolean,
    onMarkFavorite: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    when (itemState) {
        ItemState.Init -> ProductDetailsLoading()
        ItemState.Loading -> ProductDetailsLoading()
        ItemState.NotFound -> ProductDetailsNotFound()
        is ItemState.Success -> {
            ProductDetailsContent(
                modifier = modifier,
                item = itemState.item,
                isFavorite = isFavorite,
                onMarkFavorite = onMarkFavorite
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsContent(
    item: Item,
    isFavorite: Boolean,
    onMarkFavorite: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val drawableResList = Image.entries.filter { item.id in it.ids }.map { it.drawableRes }

        Box(
            contentAlignment = Alignment.Center
        ) {
            val pagerState = rememberPagerState { drawableResList.size }
            HorizontalPager(
                state = pagerState
            ) { index ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(id = if (value) ResCommon.drawable.ic_heart_active else ResCommon.drawable.ic_heart),
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

            IconButton(
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = ResCommon.drawable.ic_question),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            color = AppColors.TextGrey,
            style = CustomTypography.title1
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.subtitle,
            color = AppColors.TextBlack,
            style = CustomTypography.largeTitle1
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                id = R.string.features_product_details_available,
                item.available,
                LocalContext.current.resources.getQuantityString(R.plurals.piece, item.available)
            ),
            color = AppColors.TextGrey,
            style = CustomTypography.text1
        )

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppColors.BackgroundLightGrey
        )

        if (item.feedback != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RatingBar(
                    rating = item.feedback?.rating ?: 0f,
                    space = 2.dp,
                    painterEmpty = painterResource(id = R.drawable.rating_star),
                    painterFilled = painterResource(id = R.drawable.rating_star_filled),
                    itemSize = 20.dp,
                    gestureStrategy = GestureStrategy.None
                ) {

                }

                Text(
                    text = item.feedback?.rating.toString(),
                    color = AppColors.TextGrey,
                    style = CustomTypography.text1
                )
            }
        }
    }
}

@Composable
private fun ProductDetailsLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = 0.89f // Захардкодено из-за бага: https://issuetracker.google.com/issues/322214617
        )
    }
}

@Composable
private fun ProductDetailsNotFound(
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
                text = stringResource(id = R.string.features_product_details_not_found),
                color = AppColors.TextBlack,
                style = CustomTypography.title2
            )
        }
    }
}