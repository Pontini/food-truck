package com.pontini.food.features.chat_sdk.domain.model


sealed class ConnectionState {
        data object Connecting : ConnectionState()
        data object Init : ConnectionState()
        data object Connected : ConnectionState()
        data class FailedConnected(val message: String) : ConnectionState()
}