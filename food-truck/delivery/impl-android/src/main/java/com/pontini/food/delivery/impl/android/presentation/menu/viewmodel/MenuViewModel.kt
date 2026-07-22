package com.pontini.food.delivery.impl.android.presentation.menu.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.delivery.android.manager.DeliveryManager
import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.impl.android.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class MenuViewModel(
    private val deliveryManager: DeliveryManager
) : BaseViewModel<MenuIntent, MenuState>(MenuState()) {

    override fun dispatcher(intent: MenuIntent) {
        when (intent) {
            MenuIntent.Init -> onInit()
            is MenuIntent.AddToCart -> addToCart(intent.item)
            is MenuIntent.RemoveFromCart -> removeFromCart(intent.item)
            MenuIntent.PlaceOrder -> placeOrder()
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true, error = null) }

            try {
                val menu = deliveryManager.getMenu()
                setState { it.copy(menu = menu, isLoading = false) }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun addToCart(item: MenuItem) {
        setState { state ->
            val existing = state.cart.find { it.menuItem.id == item.id }

            val updatedCart = if (existing != null) {
                state.cart.map {
                    if (it.menuItem.id == item.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                state.cart + CartItem(
                    menuItem = item,
                    quantity = 1
                )
            }

            state.copy(cart = updatedCart)
        }
    }

    private fun removeFromCart(item: MenuItem) {
        setState { state ->
            val updatedCart = state.cart.mapNotNull {
                when {
                    it.menuItem.id != item.id -> it
                    it.quantity > 1 -> it.copy(quantity = it.quantity - 1)
                    else -> null
                }
            }

            state.copy(cart = updatedCart)
        }
    }

    private fun placeOrder() {
        val cart = state.value.cart
        if (cart.isEmpty()) return

        viewModelScope.launch {
            setState { it.copy(isPlacingOrder = true, error = null) }

            try {
                val order = deliveryManager.placeOrder(cart)
                setState {
                    it.copy(
                        isPlacingOrder = false,
                        cart = emptyList(),
                        placedOrderId = order.id
                    )
                }
            } catch (e: Exception) {
                setState { it.copy(isPlacingOrder = false, error = e.message) }
            }
        }
    }
}
