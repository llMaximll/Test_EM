package com.github.llmaximll.test_em

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.llmaximll.test_em.core.common.components.CommonTopAppBar
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.navigation.TestEmNavHost
import com.github.llmaximll.test_em.navigation.titleResOrNull
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.Test_EMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Test_EMTheme {
                val navController = rememberNavController()
                val destination = navController.currentBackStackEntryAsState().value?.destination

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
                    }
                ) { padding ->
                    TestEmNavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController
                    )
                }
            }
        }
    }
}