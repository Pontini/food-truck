package com.pontini.food.impl.android.features.chat.di

import com.pontini.food.android.manager.ChatManager
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.core.presentation.manager.ChatManagerImpl
import com.pontini.food.impl.android.core.presentation.navigate.ChatNavigatorImpl
import com.pontini.food.impl.android.features.chat.presentation.viewmodel.ChatViewModel
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatSdkPresentationModule = module {
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
    viewModel {
        ConversationsViewModel(
            repository = get()
        )
    }
}