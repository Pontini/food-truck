package com.pontini.food.delivery.impl.data.repositories

import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.Order
import com.pontini.food.delivery.domain.models.OrderStatus
import com.pontini.food.delivery.domain.repositories.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

private val NEXT_STATUS = mapOf(
    OrderStatus.RECEIVED to OrderStatus.PREPARING,
    OrderStatus.PREPARING to OrderStatus.ON_THE_WAY,
    OrderStatus.ON_THE_WAY to OrderStatus.DELIVERED
)

private const val STATUS_STEP_DELAY_MS = 3000L

class OrderRepositoryImpl(
    private val scope: CoroutineScope
) : OrderRepository {

    private val orderStatusFlows = mutableMapOf<String, MutableStateFlow<OrderStatus>>()

    override suspend fun placeOrder(items: List<CartItem>): Order {
        require(items.isNotEmpty()) { "Não é possível finalizar um pedido sem itens" }

        delay(600) // simula o envio do pedido para o food truck

        val order = Order(
            id = UUID.randomUUID().toString(),
            items = items,
            total = items.sumOf { it.subtotal },
            status = OrderStatus.RECEIVED,
            createdAt = System.currentTimeMillis()
        )

        val statusFlow = MutableStateFlow(order.status)
        orderStatusFlows[order.id] = statusFlow

        simulateOrderProgress(statusFlow)

        return order
    }

    override fun observeOrderStatus(orderId: String): Flow<OrderStatus> {
        return orderStatusFlows.getOrPut(orderId) { MutableStateFlow(OrderStatus.RECEIVED) }
    }

    private fun simulateOrderProgress(statusFlow: MutableStateFlow<OrderStatus>) {
        scope.launch {
            var current = statusFlow.value
            while (current != OrderStatus.DELIVERED) {
                delay(STATUS_STEP_DELAY_MS)
                current = NEXT_STATUS.getValue(current)
                statusFlow.value = current
            }
        }
    }
}
