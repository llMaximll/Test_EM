package com.github.llmaximll.test_em.features.favorite

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.components.CommonItemsList
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography

const val routeFavoriteScreen = "favorite"

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onProductDetailsNavigate: (String) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val favoriteItems by viewModel.favoriteItems.collectAsState(emptyList())

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var currentTab by remember { mutableStateOf(FavoriteTab.Products) }
        FavoriteTabs(
            currentTab = currentTab,
            onTabChange = {
                currentTab = it
            }
        )

        AnimatedContent(
            targetState = currentTab,
            label = "GeneralState"
        ) { tab ->
            when (tab) {
                FavoriteTab.Products -> {
                    AnimatedContent(
                        targetState = favoriteItems,
                        label = "FavoriteItems"
                    ) { items ->
                        if (items.isNotEmpty()) {
                            CommonItemsList(
                                modifier = modifier,
                                items = items,
                                favoriteItemIds = favoriteItems.map { it.id },
                                onMarkFavorite = viewModel::markItemFavorite,
                                onProductDetailsNavigate = onProductDetailsNavigate
                            )
                        } else {
                            FavoriteNotFoundState()
                        }
                    }
                }

                FavoriteTab.Brands -> Brands()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteTabs(
    currentTab: FavoriteTab,
    onTabChange: (FavoriteTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppColors.BackgroundLightGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FavoriteTab.entries.forEach { tab ->
            AnimatedContent(
                modifier = Modifier.weight(1f),
                targetState = tab == currentTab,
                label = "FavoriteTab"
            ) { isCurrent ->
                ElevatedCard(
                    modifier = Modifier
                        .padding(4.dp),
                    onClick = {
                        onTabChange(tab)
                    },
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isCurrent) AppColors.ElementWhite else AppColors.BackgroundLightGrey
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp),
                        text = stringResource(id = tab.titleRes),
                        color = if (isCurrent) AppColors.TextBlack else AppColors.TextGrey,
                        style = CustomTypography.title2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun Brands(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

    }
}

@Composable
private fun FavoriteNotFoundState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.features_favorite_not_found),
            color = AppColors.TextBlack,
            style = CustomTypography.title1
        )
    }
}