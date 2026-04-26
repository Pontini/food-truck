package com.pontini.food.impl.features.conversations.domain.model

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long
)