package com.github.llmaximll.test_em.features.product_details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.Image
import com.github.llmaximll.test_em.core.common.components.CommonPagerTabs
import com.github.llmaximll.test_em.core.common.components.DiscountElement
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import com.smarttoolfactory.ratingbar.RatingBar
import com.smarttoolfactory.ratingbar.model.GestureStrategy
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
    Box {
        var cartButtonHeight by remember { mutableStateOf(60.dp) }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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
                    verticalAlignment = Alignment.CenterVertically
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

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item.feedback?.rating.toString(),
                        color = AppColors.TextGrey,
                        style = CustomTypography.text1
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Spacer(
                        modifier = Modifier
                            .size(2.dp)
                            .background(color = AppColors.TextGrey, shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(
                            id = R.string.features_product_details_feedback,
                            item.feedback?.count ?: 0,
                            LocalContext.current.resources.getQuantityString(
                                R.plurals.feedback,
                                item.feedback?.count ?: 0
                            )
                        ),
                        color = AppColors.TextGrey,
                        style = CustomTypography.text1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.features_product_details_cost,
                        item.price?.priceWithDiscount ?: 0,
                        item.price?.unit.toString()
                    ),
                    color = AppColors.TextBlack,
                    style = CustomTypography.price
                )

                Text(
                    text = stringResource(
                        id = R.string.features_product_details_cost,
                        item.price?.price ?: 0,
                        item.price?.unit.toString()
                    ),
                    color = AppColors.TextGrey,
                    style = CustomTypography.text1,
                    textDecoration = TextDecoration.LineThrough
                )

                DiscountElement(discount = item.price?.discount ?: 0)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.features_product_details_description_label),
                color = AppColors.TextBlack,
                style = CustomTypography.title1
            )

            Spacer(modifier = Modifier.height(8.dp))

            var isDescriptionExpanded by remember { mutableStateOf(true) }
            AnimatedVisibility(
                visible = isDescriptionExpanded
            ) {
                Column {
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.BackgroundLightGrey,
                            contentColor = AppColors.TextBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.title,
                                style = CustomTypography.title2,
                                color = AppColors.TextBlack
                            )

                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = ResCommon.drawable.ic_right_arrow),
                                contentDescription = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = item.description,
                        color = AppColors.TextDarkGrey,
                        style = CustomTypography.text1
                    )
                }
            }

            AnimatedContent(
                targetState = isDescriptionExpanded,
                label = "HideDescription"
            ) { value ->
                TextButton(
                    onClick = { isDescriptionExpanded = !value },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(if (value) R.string.features_product_details_description_hide else R.string.features_product_details_description_show),
                        color = AppColors.TextGrey,
                        style = CustomTypography.button1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spec(
                infoList = item.info
            )

            Spacer(modifier = Modifier.height(12.dp))

            Structure(
                ingredients = item.ingredients
            )

            Spacer(modifier = Modifier.height(cartButtonHeight + 12.dp))
        }
        LaunchedEffect(cartButtonHeight) {
            log("cartButtonHeight: $cartButtonHeight")
        }

        val density = LocalDensity.current
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    with(density) { cartButtonHeight = it.size.height.toDp() }
                }
        ) {
            CartButton(
                priceWithDiscount = item.price?.priceWithDiscount ?: 0,
                price = item.price?.price ?: 0,
                unit = item.price?.unit.toString()
            ) {

            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Spec(
    infoList: List<Item.Info>,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.features_product_details_spec_label),
        color = AppColors.TextBlack,
        style = CustomTypography.title1
    )

    Spacer(modifier = Modifier.height(12.dp))

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        infoList.forEach { info ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = info.title,
                        color = AppColors.TextDarkGrey,
                        style = CustomTypography.text1
                    )

                    Text(
                        text = info.value,
                        color = AppColors.TextDarkGrey,
                        style = CustomTypography.text1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Divider(
                    color = AppColors.ElementLightGrey,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
private fun Structure(
    ingredients: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.features_product_details_structure_label),
            color = AppColors.TextBlack,
            style = CustomTypography.title1
        )

        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = ResCommon.drawable.ic_copy),
                contentDescription = null,
                tint = AppColors.TextGrey
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    var isIngredientsExpanded by remember { mutableStateOf(false) }
    Text(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        text = ingredients,
        color = AppColors.TextDarkGrey,
        style = CustomTypography.text1,
        maxLines = if (isIngredientsExpanded) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis
    )

    Spacer(modifier = Modifier.height(8.dp))

    val paragraph = androidx.compose.ui.text.Paragraph(
        text = ingredients,
        style = CustomTypography.text1,
        constraints = Constraints(maxWidth = LocalConfiguration.current.screenWidthDp),
        density = LocalDensity.current,
        fontFamilyResolver = LocalFontFamilyResolver.current,
    )

    LaunchedEffect(paragraph.lineCount) {
        log("paragraph.lineCount: ${paragraph.lineCount}")
    }

    if (paragraph.lineCount > 2) {
        AnimatedContent(
            targetState = isIngredientsExpanded,
            label = "HideStructure"
        ) { value ->
            TextButton(
                onClick = { isIngredientsExpanded = !value },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(if (value) R.string.features_product_details_description_hide else R.string.features_product_details_description_show),
                    color = AppColors.TextGrey,
                    style = CustomTypography.button1
                )
            }
        }
    }
}

@Composable
fun CartButton(
    priceWithDiscount: Int,
    price: Int,
    unit: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.BackgroundPink
        ),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.features_product_details_cost,
                        priceWithDiscount,
                        unit
                    ),
                    color = AppColors.TextWhite,
                    style = CustomTypography.button2
                )

                Text(
                    text = stringResource(
                        id = R.string.features_product_details_cost,
                        price,
                        unit
                    ),
                    color = AppColors.TextLightPink,
                    style = CustomTypography.caption1,
                    textDecoration = TextDecoration.LineThrough
                )
            }

            Text(
                text = stringResource(id = R.string.features_product_details_cart_button),
                color = AppColors.TextWhite,
                style = CustomTypography.button2
            )
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