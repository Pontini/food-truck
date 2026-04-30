package com.pontini.food.impl.features.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponseData(
    val id: Int,
    val name: String
)