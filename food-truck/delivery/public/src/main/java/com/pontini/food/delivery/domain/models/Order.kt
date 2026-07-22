package com.pontini.food.delivery.domain.models

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus,
    val createdAt: Long
)
