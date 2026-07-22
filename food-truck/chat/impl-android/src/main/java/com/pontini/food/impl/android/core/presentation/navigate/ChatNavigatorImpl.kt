package com.pontini.food.impl.android.core.presentation.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.food.truck.impl.android.R
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.presentation.chat.screens.ChatScreen
import com.pontini.food.impl.android.presentation.conversation.screens.ConversationsScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val CONVERSATIONS_ROUTE = "conversations"
private const val CHAT_ROUTE = "chat"
private const val CHAT_WITH_ARGS = "chat/{conversationId}/{name}"

class ChatNavigatorImpl : ChatNavigator {

    override val homeTitleRes: Int = R.string.chat_home_button

    override fun openEntryPoint(navController: NavController) {
        openConversations(navController)
    }

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