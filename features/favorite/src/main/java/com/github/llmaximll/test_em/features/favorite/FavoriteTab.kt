package com.github.llmaximll.test_em.features.favorite

import androidx.annotation.StringRes

enum class FavoriteTab(
    @StringRes val titleRes: Int
) {
    Products(
        titleRes = R.string.features_favorite_tab_products
    ),
    Brands(
        titleRes = R.string.features_favorite_tab_brands
    )
}