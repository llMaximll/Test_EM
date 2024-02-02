package com.github.llmaximll.test_em.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.llmaximll.sign_up.SignUpScreen
import com.github.llmaximll.sign_up.routeSignUpScreen

@Composable
fun TestEmNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier
            .padding(12.dp),
        navController = navController,
        startDestination = routeSignUpScreen
    ) {
        composable(
            route = routeSignUpScreen
        ) {
            SignUpScreen()
        }
    }
}