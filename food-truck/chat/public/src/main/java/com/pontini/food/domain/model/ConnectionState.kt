package com.pontini.food.domain.model


sealed class ConnectionState {

    sealed class Connection : ConnectionState() {
        data object Connecting : Connection()
        data object Connected : Connection()
        data class FailedConnected(val message: String) : Connection()
    }

    sealed class Data : ConnectionState() {
        data class MessageReceived(val message: Message) : Data()
    }
}