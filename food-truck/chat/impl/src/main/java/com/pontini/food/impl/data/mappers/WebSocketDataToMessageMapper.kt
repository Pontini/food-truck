package com.pontini.food.impl.data.mappers

import com.pontini.food.features.chat_sdk.domain.model.ChatEvent
import com.pontini.food.features.conversations.Message
import com.pontini.food.features.conversations.TypeMessage
import java.util.UUID

class WebSocketDataToMessageMapper  {
     fun map(from: String,conversationID:String): ChatEvent {
        return ChatEvent.MessageReceived(
            Message(
            id = UUID.randomUUID().toString(),
            text = from,
            conversationId =conversationID,
            senderName = "Server",
            timestamp = System.currentTimeMillis(),
            typeMessage = TypeMessage.RECEIVED
        )
        )
    }
}