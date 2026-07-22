package com.pontini.food.delivery.impl.android.presentation.menu.viewmodel

import com.pontini.food.delivery.domain.models.MenuItem

sealed interface MenuIntent {

    data object Init : MenuIntent

    data class AddToCart(val item: MenuItem) : MenuIntent

    data class RemoveFromCart(val item: MenuItem) : MenuIntent

    data object PlaceOrder : MenuIntent
}
