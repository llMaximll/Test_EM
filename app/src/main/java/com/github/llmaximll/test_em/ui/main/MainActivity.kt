package com.github.llmaximll.test_em.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.llmaximll.sign_up.routeSignUpScreen
import com.github.llmaximll.test_em.core.common.components.CommonTopAppBar
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import com.github.llmaximll.test_em.core.common.theme.Test_EMTheme
import com.github.llmaximll.test_em.features.catalog.routeCatalogScreen
import com.github.llmaximll.test_em.navigation.Destination
import com.github.llmaximll.test_em.navigation.TestEmNavHost
import com.github.llmaximll.test_em.navigation.TopLevelDestination
import com.github.llmaximll.test_em.navigation.titleResOrNull
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(

) : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Test_EMTheme {
                val navController = rememberNavController()
                val destination = navController.currentBackStackEntryAsState().value?.destination

                /*val currentTopLevelDestination = remember {
                    TopLevelDestination.entries.find { it.route == destination?.route }
                }*/

                val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

                LaunchedEffect(destination) {
                    log("CurrentDestination: ${destination?.route}")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = AppColors.BackgroundWhite,
                    contentColor = AppColors.TextGrey,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    topBar = {
                        if (destination != null) {
                            CommonTopAppBar(
                                titleRes = destination.titleResOrNull()
                            )
                        }
                    },
                    bottomBar = {
                        if (destination?.route != Destination.SignUp.route) {
                            TestEmBottomBar(
                                currentDestination = destination,
                                onNavigateToDestination = {
                                    log("TopLevelDestination: $it")
                                    navController.navigate(it.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { padding ->
                    AnimatedContent(
                        targetState = isUserLoggedIn != null,
                        label = "ScaffoldContent"
                    ) { isLoaded ->
                        if (isLoaded) {
                            TestEmNavHost(
                                modifier = Modifier.padding(padding),
                                isUserLoggedIn = requireNotNull(isUserLoggedIn),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TestEmBottomBar(
        currentDestination: NavDestination?,
        onNavigateToDestination: (TopLevelDestination) -> Unit,
        modifier: Modifier = Modifier
    ) {
        NavigationBar(
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent
        ) {
            Column {
                Divider(
                    color = AppColors.ElementLightGrey,
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TopLevelDestination.entries.forEach { destination ->
                        log("NavigationBarItem:: destination: $destination currentDestination: $currentDestination")

                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any {
                                it.route?.contains(destination.name, true) ?: false
                            } ?: false,
                            onClick = {
                                log("NavigationBarItem:: selectedDestination: $destination")
                                onNavigateToDestination(destination)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = destination.icon),
                                    contentDescription = null
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = AppColors.ElementDarkGrey,
                                selectedIconColor = AppColors.ElementPink,
                                unselectedTextColor = AppColors.TextDarkGrey,
                                selectedTextColor = AppColors.TextPink,
                                indicatorColor = AppColors.BackgroundWhite
                            ),
                            label = {
                                Text(
                                    text = stringResource(id = destination.titleRes),
                                    style = CustomTypography.caption1
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}