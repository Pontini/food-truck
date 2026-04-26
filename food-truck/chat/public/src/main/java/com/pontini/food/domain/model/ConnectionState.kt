package com.pontini.food.domain.model


sealed class ConnectionState {

    sealed class Connection : ConnectionState() {
        data object Connecting : Connection()
        data object Connected : Connection()
        data object Disconnected : Connection()
        data class Error(val message: String) : Connection()
    }

    sealed class Data : ConnectionState() {
        data class MessageReceived(val message: Message) : Data()
        data class MessageSent(val message: Message) : Data()
        data class MessageRead(val messageId: String) : Data()
        data class MessageDeleted(val messageId: String) : Data()
    }
}