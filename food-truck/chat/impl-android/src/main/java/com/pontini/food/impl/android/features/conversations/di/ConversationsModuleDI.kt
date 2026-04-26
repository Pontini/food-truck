package com.pontini.food.impl.android.features.conversations.di

import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ConversationsModuleDI = module {
    viewModel {
        ConversationsViewModel(
            repository = get()
        )
    }
}