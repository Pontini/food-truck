package com.pontini.food.impl.android.di

import com.pontini.food.android.manager.ChatManager
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.core.presentation.manager.ChatManagerImpl
import com.pontini.food.impl.android.core.presentation.navigate.ChatNavigatorImpl
import com.pontini.food.impl.android.features.chat.presentation.viewmodel.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModules = module {
    single<ChatNavigator> { ChatNavigatorImpl() }
    factory<ChatManager> {
        ChatManagerImpl(
            context = androidContext(),
            chatRepository = get()
        )
    }
    viewModel {
        ChatViewModel(
            chatManager = get()
        )
    }
}