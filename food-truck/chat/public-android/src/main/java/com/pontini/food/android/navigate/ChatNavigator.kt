
package com.pontini.food.android.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder


interface ChatNavigator {
    fun openChat(navController: NavController)
    fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}