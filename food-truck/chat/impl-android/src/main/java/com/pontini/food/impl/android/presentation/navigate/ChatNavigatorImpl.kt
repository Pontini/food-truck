package com.pontini.food.impl.android.presentation.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.presentation.screens.ChatScreen

private const val CHAT_ROUTE = "chat"

class ChatNavigatorImpl : ChatNavigator {
    override fun openChat(navController: NavController) {
        navController.navigate(CHAT_ROUTE)
    }

    override fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.composable(CHAT_ROUTE) {
            ChatScreen()
        }
    }
}