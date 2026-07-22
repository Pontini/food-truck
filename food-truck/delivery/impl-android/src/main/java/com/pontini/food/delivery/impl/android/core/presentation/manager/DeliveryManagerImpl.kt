package com.pontini.food.delivery.impl.android.core.presentation.manager

import com.pontini.food.delivery.android.manager.DeliveryManager
import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.domain.models.Order
import com.pontini.food.delivery.domain.models.OrderStatus
import com.pontini.food.delivery.domain.repositories.MenuRepository
import com.pontini.food.delivery.domain.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow

class DeliveryManagerImpl(
    private val menuRepository: MenuRepository,
    private val orderRepository: OrderRepository
) : DeliveryManager {

    override suspend fun getMenu(): List<MenuItem> {
        return menuRepository.getMenu()
    }

    override suspend fun placeOrder(items: List<CartItem>): Order {
        return orderRepository.placeOrder(items)
    }

    override fun observeOrderStatus(orderId: String): Flow<OrderStatus> {
        return orderRepository.observeOrderStatus(orderId)
    }
}
