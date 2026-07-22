package com.pontini.food.delivery.impl.android.di

import com.pontini.food.core.navigation.FeatureNavigator
import com.pontini.food.delivery.android.manager.DeliveryManager
import com.pontini.food.delivery.android.navigate.DeliveryNavigator
import com.pontini.food.delivery.impl.android.core.presentation.manager.DeliveryManagerImpl
import com.pontini.food.delivery.impl.android.core.presentation.navigate.DeliveryNavigatorImpl
import com.pontini.food.delivery.impl.android.presentation.menu.viewmodel.MenuViewModel
import com.pontini.food.delivery.impl.android.presentation.order.viewmodel.OrderStatusViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val deliverySdkPresentationModule = module {

    single<DeliveryNavigator> { DeliveryNavigatorImpl() } bind FeatureNavigator::class

    single<DeliveryManager> {
        DeliveryManagerImpl(
            menuRepository = get(),
            orderRepository = get()
        )
    }

    viewModel {
        MenuViewModel(
            deliveryManager = get()
        )
    }

    viewModel {
        OrderStatusViewModel(
            deliveryManager = get()
        )
    }
}
