package com.pontini.food.impl.features.chat_sdk.data.mappers

import com.pontini.food.domain.model.Message
import com.pontini.food.mapper.Mapper
import java.util.UUID

class IncomingMessageMapper : Mapper<String, Message> {
    override fun map(from: String): Message {
        return Message(
            id = UUID.randomUUID().toString(),
            text = from,
            sender = "Server"
        )
    }
}