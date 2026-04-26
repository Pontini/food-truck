package com.pontini.food.impl.android.di

import com.pontini.food.impl.di.chatCoreModules
import org.koin.dsl.module

val chatModule = module {
    includes(presentationModules)
    includes(chatCoreModules)
}