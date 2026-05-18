package com.pontini.food.domain.models


sealed class ConnectionStatus {
        data object Connecting : ConnectionStatus()
        data object Init : ConnectionStatus()
        data object Connected : ConnectionStatus()
        data class FailedConnected(val message: String) : ConnectionStatus()
}