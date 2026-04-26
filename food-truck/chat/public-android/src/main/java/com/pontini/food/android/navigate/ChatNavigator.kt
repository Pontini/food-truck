package com.pontini.food.android.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface ChatNavigator {

    fun openConversations(navController: NavController)

    fun openChat(
        navController: NavController,
        conversationId: String,
        name: String
    )

    fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}