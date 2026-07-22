package com.pontini.food.delivery.impl.android.di

import com.pontini.food.delivery.impl.di.deliverySdkModule
import org.koin.dsl.module

val deliveryModule = module {
    includes(deliverySdkModule)
    includes(deliverySdkPresentationModule)
}
