package com.pontini.food.foodtruck

import android.app.Application
import com.pontini.food.impl.android.di.chatModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FoodTruckApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FoodTruckApplication)
            modules(
                chatModule
            )
        }
    }
}