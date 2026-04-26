package com.pontini.food.foodtruck.presentation.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pontini.food.android.navigate.ChatNavigator

const val HOME_ROUTE = "home"

@Composable
fun AppNavHost(
    chatNavigator: ChatNavigator
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        composable(HOME_ROUTE) {
            HomeScreen(
                onNavigateToChat = {
                    chatNavigator.openConversations(navController)
                }
            )
        }

        chatNavigator.registerGraph(navController, this)
    }
}