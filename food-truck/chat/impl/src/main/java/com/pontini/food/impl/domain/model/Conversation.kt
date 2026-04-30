package com.pontini.food.impl.domain.model

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long
)

data class ConversationResult(
    val data: List<Conversation>,
    val source: Source
)

enum class Source {
    CACHE,
    REMOTE
}