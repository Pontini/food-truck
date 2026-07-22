package com.pontini.food.delivery.android.manager

import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.domain.models.Order
import com.pontini.food.delivery.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow

interface DeliveryManager {

    suspend fun getMenu(): List<MenuItem>

    suspend fun placeOrder(items: List<CartItem>): Order

    fun observeOrderStatus(orderId: String): Flow<OrderStatus>
}
