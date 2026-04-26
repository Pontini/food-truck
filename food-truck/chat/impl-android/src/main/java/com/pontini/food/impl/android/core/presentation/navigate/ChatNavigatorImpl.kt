package com.pontini.food.impl.android.core.presentation.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.features.chat.presentation.screens.ChatScreen
import com.pontini.food.impl.android.features.conversations.presentation.screens.ConversationsScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val CONVERSATIONS_ROUTE = "conversations"
private const val CHAT_ROUTE = "chat"
private const val CHAT_WITH_ARGS = "chat/{conversationId}/{name}"

class ChatNavigatorImpl : ChatNavigator {

    override fun openChat(
        navController: NavController,
        conversationId: String,
        name: String
    ) {
        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())

        navController.navigate("$CHAT_ROUTE/$conversationId/$encodedName")
    }

    override fun openConversations(navController: NavController) {
        navController.navigate(CONVERSATIONS_ROUTE)
    }

    override fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {

        // 🟢 TELA DE CONVERSAS
        navGraphBuilder.composable(CONVERSATIONS_ROUTE) {
            ConversationsScreen(
                onOpenChat = { conversation ->
                    openChat(
                        navController = navController,
                        conversationId = conversation.id,
                        name = conversation.name
                    )
                }
            )
        }

        // 💬 TELA DE CHAT
        navGraphBuilder.composable(
            route = CHAT_WITH_ARGS,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val conversationId =
                backStackEntry.arguments?.getString("conversationId") ?: ""

            val name =
                backStackEntry.arguments?.getString("name") ?: ""

            ChatScreen(
                conversationId = conversationId,
                name = name
            )
        }
    }
}