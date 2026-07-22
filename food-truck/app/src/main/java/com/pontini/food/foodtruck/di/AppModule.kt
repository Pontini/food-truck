package com.pontini.food.foodtruck.di

import com.pontini.food.core.navigation.FeatureNavigator
import org.koin.dsl.module

val appModule = module {
    single<List<FeatureNavigator>> { getAll() }
}
