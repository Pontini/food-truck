package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import com.pontini.food.impl.domain.model.Conversation

data class ConversationsState(
    val conversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)