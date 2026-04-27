package com.pontini.food.impl.features.conversations.domain.model

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long
)

sealed class ConversationResult {
    data class Success(
        val data: List<Conversation>,
        val source: Source
    ) : ConversationResult()
    data class Error(val message: String) : ConversationResult()
    data object Loading : ConversationResult()
}

enum class Source {
    CACHE,
    REMOTE
}