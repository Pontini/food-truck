package com.pontini.food.delivery.impl.di

import com.pontini.food.delivery.domain.repositories.MenuRepository
import com.pontini.food.delivery.domain.repositories.OrderRepository
import com.pontini.food.delivery.impl.data.repositories.MenuRepositoryImpl
import com.pontini.food.delivery.impl.data.repositories.OrderRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val DELIVERY_SCOPE = named("DELIVERY_SCOPE")

val deliverySdkModule = module {

    single<CoroutineScope>(DELIVERY_SCOPE) { DeliveryScope() }

    single<MenuRepository> { MenuRepositoryImpl() }

    single<OrderRepository> {
        OrderRepositoryImpl(
            scope = get(DELIVERY_SCOPE)
        )
    }
}

class DeliveryScope : CoroutineScope {
    override val coroutineContext = SupervisorJob() + Dispatchers.Default
}
