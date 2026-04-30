package com.pontini.food.impl.android.di

import com.pontini.food.impl.android.core.presentation.room.externalModules
import com.pontini.food.impl.android.features.chat.di.chatSdkPresentationModule
import org.koin.dsl.module

val chatModule = module {
    includes(com.pontini.food.impl.core.mapper.di.chatSdkModule)
    includes(chatSdkPresentationModule)
    includes(com.pontini.food.impl.core.mapper.di.conversationsModuleData)
    includes(externalModules)
}
