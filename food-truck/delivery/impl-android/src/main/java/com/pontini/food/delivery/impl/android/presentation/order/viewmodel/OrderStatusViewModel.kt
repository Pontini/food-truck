package com.pontini.food.delivery.impl.android.presentation.order.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.delivery.android.manager.DeliveryManager
import com.pontini.food.delivery.impl.android.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class OrderStatusViewModel(
    private val deliveryManager: DeliveryManager
) : BaseViewModel<OrderStatusIntent, OrderStatusState>(OrderStatusState()) {

    override fun dispatcher(intent: OrderStatusIntent) {
        when (intent) {
            is OrderStatusIntent.Init -> onInit(intent.orderId)
        }
    }

    private fun onInit(orderId: String) {
        setState { it.copy(orderId = orderId) }

        viewModelScope.launch {
            deliveryManager.observeOrderStatus(orderId).collect { status ->
                setState { it.copy(status = status) }
            }
        }
    }
}
