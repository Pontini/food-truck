package com.pontini.food.foodtruck.presentation.navigation

import HomeAction
import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pontini.food.core.navigation.FeatureNavigator

const val HOME_ROUTE = "home"

@Composable
fun AppNavHost(
    featureNavigators: List<FeatureNavigator>
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        composable(HOME_ROUTE) {
            HomeScreen(
                actions = featureNavigators.map { navigator ->
                    HomeAction(
                        titleRes = navigator.homeTitleRes,
                        onClick = { navigator.openEntryPoint(navController) }
                    )
                }
            )
        }

        featureNavigators.forEach { navigator ->
            navigator.registerGraph(navController, this)
        }
    }
}
