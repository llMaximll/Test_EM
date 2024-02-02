package com.github.llmaximll.test_em.features.catalog

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.theme.AppColors
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

const val routeCatalogScreen = "catalog"
const val cardHeight = 200

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val itemsState by viewModel.itemsFetchState.collectAsState()

    LaunchedEffect(itemsState) {
        log("ItemsState: $itemsState")
    }

    Content(
        itemsState = itemsState
    )
}

@Composable
private fun Content(
    itemsState: FetchState<List<Item>>,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = itemsState,
        label = "Catalog"
    ) { state ->
        when (state) {
            FetchState.Init -> LoadingList()
            FetchState.Loading -> LoadingList()
            is FetchState.Error -> LoadingList()
            is FetchState.Success -> ContentList(state.data)
        }
    }
}

@Composable
private fun ContentList(
    items: List<Item>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            ContentItem(
                item = item
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ContentItem(
    item: Item,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight.dp),
        onClick = {

        },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = AppColors.BackgroundLightGrey
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = AppColors.BackgroundWhite
        )
    ) {
        val drawableResList = Image.entries.filter { item.id in it.ids }.map { it.drawableRes }

        HorizontalPager(
            state = rememberPagerState { 2 }
        ) { index ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                painter = painterResource(
                    id = drawableResList[index]
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun LoadingList(
    modifier: Modifier = Modifier
) {
    val shimmerAnimatable = remember { Animatable(Color.LightGray.copy(alpha = 0.4f)) }

    LaunchedEffect(Unit) {
        shimmerAnimatable.animateTo(
            targetValue = Color.LightGray,
            animationSpec = infiniteRepeatable(
                animation = tween(700),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(8) {
            LoadingItem()
        }
    }
}

@Composable
private fun LoadingItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect()
    ) {

    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val startOffset by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "ShimmerOffset"
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray,
                Color.LightGray.copy(alpha = 0.7f),
                Color.LightGray,
            ),
            start = Offset(startOffset, 0f),
            end = Offset(startOffset + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}