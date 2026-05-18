package com.pontini.food.impl.android.presentation.conversation.viewmodel

sealed interface ConversationsIntent {
    data object Init : ConversationsIntent

}