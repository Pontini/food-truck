
package com.pontini.food.android.navigate


interface ChatNavigator {

    fun openChat(navController: NavController)

    fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}