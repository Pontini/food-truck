package com.pontini.food.delivery.impl.android.presentation.order.viewmodel

import com.pontini.food.delivery.domain.models.OrderStatus

data class OrderStatusState(
    val orderId: String = "",
    val status: OrderStatus = OrderStatus.RECEIVED
)
