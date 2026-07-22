package com.pontini.food.delivery.impl.android.presentation.order.viewmodel

sealed interface OrderStatusIntent {
    data class Init(val orderId: String) : OrderStatusIntent
}
