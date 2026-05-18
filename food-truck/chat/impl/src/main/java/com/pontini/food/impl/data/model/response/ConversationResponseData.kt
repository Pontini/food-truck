package com.pontini.food.impl.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponseData(
    val id: Int,
    val name: String
)