package com.github.llmaximll.test_em.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.llmaximll.sign_up.SignUpScreen
import com.github.llmaximll.sign_up.routeSignUpScreen
import com.github.llmaximll.test_em.features.catalog.CatalogScreen
import com.github.llmaximll.test_em.features.catalog.routeCatalogScreen
import com.github.llmaximll.test_em.features.main.MainScreen
import com.github.llmaximll.test_em.features.main.routeMainScreen

@Composable
fun TestEmNavHost(
    modifier: Modifier = Modifier,
    isUserLoggedIn: Boolean,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier
            .padding(12.dp),
        navController = navController,
        startDestination = if (isUserLoggedIn) routeMainScreen else routeSignUpScreen,
        contentAlignment = Alignment.Center
    ) {
        composable(
            route = routeSignUpScreen
        ) {
            SignUpScreen(
                onSignUp = {
                    navController.navigate(routeMainScreen) {
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
            route = routeMainScreen
        ) {
            MainScreen()
        }

        composable(
            route = routeCatalogScreen
        ) {
            CatalogScreen()
        }
    }
}