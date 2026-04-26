package com.pontini.food.impl.android.di

import com.pontini.food.impl.android.core.presentation.room.externalModules
import com.pontini.food.impl.android.features.chat.di.chatSdkPresentationModule
import com.pontini.food.impl.features.chat_sdk.di.chatSdkModule
import com.pontini.food.impl.features.conversations.di.conversationsModuleData
import org.koin.dsl.module

val chatModule = module {
    includes(chatSdkModule)
    includes(chatSdkPresentationModule)
    includes(conversationsModuleData)
    includes(externalModules)
}