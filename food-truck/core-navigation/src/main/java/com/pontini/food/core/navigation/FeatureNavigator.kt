package com.pontini.food.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface FeatureNavigator {

    val homeTitleRes: Int

    fun openEntryPoint(navController: NavController)

    fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}
