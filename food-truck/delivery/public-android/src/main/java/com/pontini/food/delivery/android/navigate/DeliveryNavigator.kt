package com.pontini.food.delivery.android.navigate

import androidx.navigation.NavController
import com.pontini.food.core.navigation.FeatureNavigator

interface DeliveryNavigator : FeatureNavigator {

    fun openMenu(navController: NavController)

    fun openOrderStatus(
        navController: NavController,
        orderId: String
    )
}
