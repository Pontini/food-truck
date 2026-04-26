package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

sealed interface ConversationsIntent {
    data object Init : ConversationsIntent

}