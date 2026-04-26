package com.pontini.food.impl.android.core.presentation.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.features.chat.presentation.screens.ChatScreen
import com.pontini.food.impl.android.features.conversations.presentation.screens.ConversationsScreen

private const val CONVERSATIONS_ROUTE = "conversations"
private const val CHAT_ROUTE = "chat"
private const val CHAT_WITH_ID = "chat/{conversationId}"

class ChatNavigatorImpl : ChatNavigator {

    override fun openChat(navController: NavController, conversationId: String) {
        navController.navigate("$CHAT_ROUTE/$conversationId")
    }

    override fun openConversations(navController: NavController) {
        navController.navigate(CONVERSATIONS_ROUTE)
    }

    override fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {

        // 🟢 TELA INICIAL
        navGraphBuilder.composable(CONVERSATIONS_ROUTE) {
            ConversationsScreen(
                onOpenChat = { id ->
                    openChat(navController, id)
                }
            )
        }

        navGraphBuilder.composable(
            route = CHAT_WITH_ID,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: ""

            ChatScreen(
                conversationId = conversationId
            )
        }
    }
}