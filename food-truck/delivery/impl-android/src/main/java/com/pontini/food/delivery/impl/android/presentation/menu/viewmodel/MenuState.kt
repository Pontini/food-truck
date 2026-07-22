package com.pontini.food.delivery.impl.android.presentation.menu.viewmodel

import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.MenuItem

data class MenuState(
    val menu: List<MenuItem> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val error: String? = null,
    val placedOrderId: String? = null
) {
    val total: Double get() = cart.sumOf { it.subtotal }
    val itemCount: Int get() = cart.sumOf { it.quantity }
}
