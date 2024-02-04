package com.github.llmaximll.test_em.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import com.github.llmaximll.sign_up.routeSignUpScreen
import com.github.llmaximll.test_em.R
import com.github.llmaximll.test_em.features.cart.routeCartScreen
import com.github.llmaximll.test_em.features.catalog.routeCatalogScreen
import com.github.llmaximll.test_em.features.discount.routeDiscountScreen
import com.github.llmaximll.test_em.features.favorite.routeFavoriteScreen
import com.github.llmaximll.test_em.features.main.routeMainScreen
import com.github.llmaximll.test_em.features.product_details.routeProductDetailsScreen
import com.github.llmaximll.test_em.features.profile.routeProfileScreen
import com.github.llmaximll.test_em.core.common.R as ResCommon

enum class Destination(
    @StringRes val titleRes: Int,
    val route: String
) {
    SignUp(
        titleRes = R.string.destination_title_sign_up,
        route = routeSignUpScreen
    ),
    Main(
        titleRes = R.string.destination_title_main,
        route = routeMainScreen
    ),
    Catalog(
        titleRes = R.string.destination_title_catalog,
        route = routeCatalogScreen
    ),
    Cart(
        titleRes = R.string.destination_title_cart,
        route = routeCartScreen
    ),
    Discount(
        titleRes = R.string.destination_title_discount,
        route = routeDiscountScreen
    ),
    Profile(
        titleRes = R.string.destination_title_profile,
        route = routeProfileScreen
    ),
    ProductDetails(
        titleRes = R.string.destination_title_product_details,
        route = routeProductDetailsScreen
    ),
    Favorite(
        titleRes = R.string.destination_title_favorite,
        route = routeFavoriteScreen
    )
}

enum class TopLevelDestination(
    @StringRes val titleRes: Int,
    val route: String,
    @DrawableRes val icon: Int
) {
    Main(
        titleRes = Destination.Main.titleRes,
        route = Destination.Main.route,
        icon = ResCommon.drawable.ic_home
    ),
    Catalog(
        titleRes = Destination.Catalog.titleRes,
        route = Destination.Catalog.route,
        icon = ResCommon.drawable.ic_catalog
    ),
    Cart(
        titleRes = Destination.Cart.titleRes,
        route = Destination.Cart.route,
        icon = ResCommon.drawable.ic_bag
    ),
    Discount(
        titleRes = Destination.Discount.titleRes,
        route = Destination.Discount.route,
        icon = ResCommon.drawable.ic_discont
    ),
    Profile(
        titleRes = Destination.Profile.titleRes,
        route = Destination.Profile.route,
        icon = ResCommon.drawable.ic_account
    ),
}

@Composable
fun NavDestination?.titleResOrNull(): Int? =
    if (this?.route != routeProfileScreen)
        Destination.entries.find { it.route == this?.route }?.titleRes
    else
        R.string.destination_title_profile_top_bar