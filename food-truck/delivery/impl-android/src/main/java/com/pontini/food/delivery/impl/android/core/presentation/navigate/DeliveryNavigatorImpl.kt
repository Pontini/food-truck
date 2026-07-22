package com.pontini.food.delivery.impl.android.core.presentation.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pontini.food.delivery.android.navigate.DeliveryNavigator
import com.pontini.food.delivery.impl.android.R
import com.pontini.food.delivery.impl.android.presentation.menu.screens.MenuScreen
import com.pontini.food.delivery.impl.android.presentation.order.screens.OrderStatusScreen

private const val MENU_ROUTE = "delivery/menu"
private const val ORDER_STATUS_ROUTE = "delivery/order"
private const val ORDER_STATUS_WITH_ARGS = "$ORDER_STATUS_ROUTE/{orderId}"

class DeliveryNavigatorImpl : DeliveryNavigator {

    override val homeTitleRes: Int = R.string.delivery_home_button

    override fun openEntryPoint(navController: NavController) {
        openMenu(navController)
    }

    override fun openMenu(navController: NavController) {
        navController.navigate(MENU_ROUTE)
    }

    override fun openOrderStatus(
        navController: NavController,
        orderId: String
    ) {
        navController.navigate("$ORDER_STATUS_ROUTE/$orderId")
    }

    override fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {

        navGraphBuilder.composable(MENU_ROUTE) {
            MenuScreen(
                onOrderPlaced = { orderId ->
                    openOrderStatus(navController, orderId)
                }
            )
        }

        navGraphBuilder.composable(
            route = ORDER_STATUS_WITH_ARGS,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""

            OrderStatusScreen(
                orderId = orderId,
                onBackToHome = {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                }
            )
        }
    }
}
