package com.pontini.food.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponseData(
    val id: Int,
    val name: String
)