package com.pontini.food.impl.core.mapper.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponseData(
    val id: Int,
    val name: String
)