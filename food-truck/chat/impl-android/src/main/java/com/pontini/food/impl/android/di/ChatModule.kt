package com.pontini.food.impl.android.di

import com.pontini.food.impl.android.core.presentation.room.externalModules
import com.pontini.food.impl.di.dataModules
import org.koin.dsl.module

val chatModule = module {
    includes(presentationModules)
    includes(dataModules)
    includes(externalModules)
}