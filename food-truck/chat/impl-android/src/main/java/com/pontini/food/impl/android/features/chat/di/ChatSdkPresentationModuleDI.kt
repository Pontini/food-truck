package com.pontini.food.impl.android.features.chat.di

import com.pontini.food.android.manager.ChatManager
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.impl.android.core.presentation.manager.ChatManagerImpl
import com.pontini.food.impl.android.core.presentation.navigate.ChatNavigatorImpl
import com.pontini.food.impl.android.features.chat.data.datasource.impl.local.ChatLocalDataSourceImpl
import com.pontini.food.impl.android.features.chat.data.mappers.MessageToEntityMapper
import com.pontini.food.impl.android.features.chat.data.model.room.MessageEntity
import com.pontini.food.impl.android.features.chat.presentation.viewmodel.ChatViewModel
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsViewModel
import com.pontini.food.impl.android.observability.ObservabilityFacadeImpl
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import com.pontini.food.mapper.Mapper
import com.pontini.food.observability.ObservabilityFacade
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val MESSAGE_TO_ENTITY = named("MESSAGE_TO_ENTITY")


val chatSdkPresentationModule = module {
    single<ChatNavigator> { ChatNavigatorImpl() }

    single<Mapper<SendMessageRequest, MessageEntity>>(MESSAGE_TO_ENTITY) {
        MessageToEntityMapper()
    }

    factory<ChatManager> {
        ChatManagerImpl(
            context = androidContext(),
            chatRepository = get()
        )
    }

    factory<ChatLocalDataSource> {
        ChatLocalDataSourceImpl(
            dao = get(),
            messageToEntityMapper = get(MESSAGE_TO_ENTITY)
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

    single<ObservabilityFacade> {
        ObservabilityFacadeImpl(
            telemetry = get()
        )
    }
}