package com.pontini.food.impl.android.di

import com.pontini.food.impl.android.core.presentation.room.externalModules
import com.pontini.food.impl.di.chatSdkModule
import com.pontini.food.impl.di.conversationsModuleData
import org.koin.dsl.module

val chatModule = module {
    includes(chatSdkModule)
    includes(chatSdkPresentationModule)
    includes(conversationsModuleData)
    includes(externalModules)
}
