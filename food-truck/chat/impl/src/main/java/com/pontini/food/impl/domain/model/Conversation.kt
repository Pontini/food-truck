package com.pontini.food.impl.domain.model

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long
)