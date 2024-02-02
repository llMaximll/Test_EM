package com.github.llmaximll.test_em.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import com.github.llmaximll.sign_up.routeSignUpScreen
import com.github.llmaximll.test_em.R

enum class Destinations(
    @StringRes val titleRes: Int,
    val route: String
) {
    SignUp(
        titleRes = R.string.destination_title_sign_up,
        route = routeSignUpScreen
    )
}

@Composable
fun NavDestination?.titleResOrNull(): Int? =
    Destinations.entries.find { it.route == this?.route }?.titleRes