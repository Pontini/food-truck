package com.pontini.food.impl.features.chat_sdk.data.mappers

import com.pontini.food.domain.model.Message
import com.pontini.food.domain.model.TypeMessage
import com.pontini.food.mapper.Mapper
import java.util.UUID

class WebSocketDataToMessageMapper  {
     fun map(from: String,conversationID:String): Message {
         println("conversationID in mapper: $conversationID")
        return Message(
            id = UUID.randomUUID().toString(),
            text = from,
            conversationId =conversationID,
            senderName = "Server",
            timestamp = System.currentTimeMillis(),
            typeMessage = TypeMessage.RECEIVED
        )
    }
}