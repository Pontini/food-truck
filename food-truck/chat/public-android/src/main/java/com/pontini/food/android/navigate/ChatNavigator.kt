package com.pontini.food.android.navigate

import androidx.navigation.NavController
import com.pontini.food.core.navigation.FeatureNavigator

interface ChatNavigator : FeatureNavigator {

    fun openConversations(navController: NavController)

    fun openChat(
        navController: NavController,
        conversationId: String,
        name: String
    )
}