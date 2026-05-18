package com.pontini.food.impl.android.presentation.conversation.viewmodel

import com.pontini.food.domain.models.Conversation


data class ConversationsState(
    val conversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.OfflineNoData,
    val error: String? = null
)

sealed class ConnectionStatus {
    data object Online : ConnectionStatus()
    data object OfflineWithCache : ConnectionStatus()
    data object Connecting : ConnectionStatus()
    data object OfflineNoData : ConnectionStatus()
}