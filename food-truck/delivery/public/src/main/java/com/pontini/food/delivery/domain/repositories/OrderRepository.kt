package com.pontini.food.delivery.domain.repositories

import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.Order
import com.pontini.food.delivery.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun placeOrder(items: List<CartItem>): Order

    fun observeOrderStatus(orderId: String): Flow<OrderStatus>
}
