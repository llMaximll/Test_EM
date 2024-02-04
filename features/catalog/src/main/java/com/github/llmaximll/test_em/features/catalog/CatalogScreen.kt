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
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.components.CommonPagerTabs
import com.github.llmaximll.test_em.core.common.components.DiscountElement
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import com.github.llmaximll.test_em.core.common.R as ResCommon

const val routeCatalogScreen = "catalog"

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    onProductDetailsNavigate: (String) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val itemsState by viewModel.itemsFetchState.collectAsState()
    val favoriteItemIds by viewModel.favoriteItemIds.collectAsState(emptyList())
    val currentSort by viewModel.currentSort.collectAsState()
    val currentTag by viewModel.currentTag.collectAsState()

    LaunchedEffect(itemsState) {
        log("ItemsState: $itemsState")
    }

    Content(
        modifier = modifier,
        currentSort = currentSort,
        currentTag = currentTag,
        itemsState = itemsState,
        favoriteItemIds = favoriteItemIds,
        onMarkFavorite = viewModel::markItemFavorite,
        onTryAgainRequest = viewModel::fetchItemsRemote,
        onSortChange = viewModel::changeSort,
        onTagChange = viewModel::changeTag,
        onProductDetailsNavigate = onProductDetailsNavigate
    )
}

@Composable
private fun Content(
    itemsState: FetchState<List<Item>>,
    currentSort: Sort,
    currentTag: Tag?,
    favoriteItemIds: List<String>,
    onMarkFavorite: (String, Boolean) -> Unit,
    onTryAgainRequest: () -> Unit,
    onSortChange: (Sort) -> Unit,
    onTagChange: (Tag?) -> Unit,
    onProductDetailsNavigate: (String) -> Unit,
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
            is FetchState.Error -> ErrorContent(
                onTryAgain = onTryAgainRequest
            )

            is FetchState.Success -> ContentList(
                items = state.data,
                favoriteItemIds = favoriteItemIds,
                currentSort = currentSort,
                currentTag = currentTag,
                onMarkFavorite = onMarkFavorite,
                onSortChange = onSortChange,
                onTagChange = onTagChange,
                onProductDetailsNavigate = onProductDetailsNavigate
            )
        }
    }
}

@Composable
private fun ContentList(
    items: List<Item>,
    favoriteItemIds: List<String>,
    currentSort: Sort,
    currentTag: Tag?,
    onMarkFavorite: (String, Boolean) -> Unit,
    onSortChange: (Sort) -> Unit,
    onTagChange: (Tag?) -> Unit,
    onProductDetailsNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Sort(
            currentSort = currentSort,
            onSortChange = onSortChange
        )

        Tags(
            selectedTag = currentTag,
            onTagChange = onTagChange
        )

        val density = LocalDensity.current
        val lazyGridState = rememberLazyGridState()

        LazyVerticalGrid(
            modifier = Modifier,
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

                ContentItem(
                    modifier = Modifier.height(maxHeightInRowDp),
                    item = item,
                    isFavorite = item.id in favoriteItemIds,
                    onMarkFavorite = onMarkFavorite,
                    onProductDetailsNavigate = onProductDetailsNavigate
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ContentItem(
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
                            painter = painterResource(id = ResCommon.drawable.ic_small_star),
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
                    painter = painterResource(id = ResCommon.drawable.ic_plus),
                    contentDescription = null,
                    tint = AppColors.ElementWhite
                )
            }
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
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect()
    ) {

    }
}

@Composable
private fun ErrorContent(
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.features_catalog_request_error),
                color = AppColors.TextBlack,
                style = CustomTypography.title1
            )

            Button(
                onClick = onTryAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.ElementPink,
                    contentColor = AppColors.TextWhite
                )
            ) {
                Text(
                    text = stringResource(id = ResCommon.string.core_common_try_again),
                    color = AppColors.TextWhite,
                    style = CustomTypography.title3
                )
            }
        }
    }
}

@Composable
private fun Sort(
    currentSort: Sort,
    onSortChange: (Sort) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            var isMenuExpanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = {
                    isMenuExpanded = true
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = ResCommon.drawable.ic_sort_by),
                    contentDescription = null,
                    tint = AppColors.ElementBlack
                )

                Text(
                    text = stringResource(id = currentSort.titleRes),
                    color = AppColors.TextDarkGrey,
                    style = CustomTypography.title4
                )

                Icon(
                    painter = painterResource(id = ResCommon.drawable.ic_down_arrow),
                    contentDescription = null,
                    tint = AppColors.ElementBlack
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                Sort.entries.filterNot { it == Sort.Standard }.forEach { sort ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = sort.titleRes),
                                style = CustomTypography.title3,
                                color = AppColors.TextBlack
                            )
                        },
                        onClick = {
                            onSortChange(sort)
                            isMenuExpanded = false
                        }
                    )
                }
            }
        }

        TextButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = ResCommon.drawable.ic_filter),
                contentDescription = null,
                tint = AppColors.ElementBlack
            )

            Text(
                text = stringResource(id = R.string.features_catalog_filter),
                color = AppColors.TextDarkGrey,
                style = CustomTypography.title4
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tags(
    selectedTag: Tag?,
    onTagChange: (Tag?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Tag.entries.forEach { tag ->
            AnimatedContent(
                targetState = tag == selectedTag,
                label = "Tags"
            ) { isSelected ->
                InputChip(
                    selected = isSelected,
                    onClick = { onTagChange(tag) },
                    label = {
                        Text(
                            text = stringResource(id = tag.titleRes)
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = AppColors.BackgroundLightGrey,
                        selectedContainerColor = AppColors.ElementDarkBlue,
                        labelColor = AppColors.TextGrey,
                        selectedLabelColor = AppColors.TextWhite
                    ),
                    border = InputChipDefaults.inputChipBorder(
                        borderColor = AppColors.BackgroundLightGrey,
                        selectedBorderColor = AppColors.ElementDarkBlue
                    ),
                    trailingIcon = {
                        if (isSelected) {
                            IconButton(
                                modifier = Modifier.size(20.dp),
                                onClick = { onTagChange(null) }
                            ) {
                                Icon(
                                    painter = painterResource(id = ResCommon.drawable.ic_small_close),
                                    contentDescription = null,
                                    tint = AppColors.ElementWhite
                                )
                            }
                        }
                    }
                )
            }
        }
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