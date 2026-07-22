package com.pontini.food.delivery.domain.models

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
) {
    val subtotal: Double get() = menuItem.price * quantity
}
