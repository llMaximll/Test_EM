package com.github.llmaximll.test_em.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.llmaximll.sign_up.SignUpScreen
import com.github.llmaximll.sign_up.routeSignUpScreen
import com.github.llmaximll.test_em.core.common.ext.asUrlDecoded
import com.github.llmaximll.test_em.core.common.ext.asUrlEncoded
import com.github.llmaximll.test_em.features.cart.CartScreen
import com.github.llmaximll.test_em.features.cart.routeCartScreen
import com.github.llmaximll.test_em.features.catalog.CatalogScreen
import com.github.llmaximll.test_em.features.catalog.routeCatalogScreen
import com.github.llmaximll.test_em.features.discount.DiscountScreen
import com.github.llmaximll.test_em.features.discount.routeDiscountScreen
import com.github.llmaximll.test_em.features.main.MainScreen
import com.github.llmaximll.test_em.features.main.routeMainScreen
import com.github.llmaximll.test_em.features.product_details.ProductDetailsScreen
import com.github.llmaximll.test_em.features.product_details.routeProductDetailsScreen
import com.github.llmaximll.test_em.features.profile.ProfileScreen
import com.github.llmaximll.test_em.features.profile.routeProfileScreen

@Composable
fun TestEmNavHost(
    modifier: Modifier = Modifier,
    isUserLoggedIn: Boolean,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier
            .padding(horizontal = 12.dp),
        navController = navController,
        startDestination = if (isUserLoggedIn) Destination.Main.route else Destination.SignUp.route,
        contentAlignment = Alignment.Center
    ) {
        composable(
            route = Destination.SignUp.route
        ) {
            SignUpScreen(
                onSignUp = {
                    navController.navigate(Destination.Main.route) {
                        navController.graph.findStartDestination().route?.let { route ->
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

        composable(
            route = Destination.Main.route
        ) {
            MainScreen()
        }

        composable(
            route = Destination.Catalog.route
        ) {
            CatalogScreen(
                onProductDetailsNavigate = { productId ->
                    navController.navigate("${routeProductDetailsScreen}/${productId.asUrlEncoded()}")
                }
            )
        }

        composable(
            route = routeCartScreen
        ) {
            CartScreen()
        }

        composable(
            route = "${Destination.ProductDetails.route}/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.asUrlDecoded()

            ProductDetailsScreen(
                productId = productId
            )
        }

        composable(
            route = Destination.Discount.route
        ) {
            DiscountScreen()
        }

        composable(
            route = Destination.Profile.route
        ) {
            ProfileScreen(
                onExit = {
                    navController.navigate(Destination.SignUp.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}