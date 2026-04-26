package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val repository: ConversationRepository
) : BaseViewModel<ConversationsIntent, ConversationsState>(ConversationsState()) {

    override fun dispatcher(intent: ConversationsIntent) {
        when (intent) {
            ConversationsIntent.Init -> {
                onInit()
            }
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }
            repository.getConversations().collect { list ->
                setState { state ->
                    state.copy(
                        conversations = list,
                        isLoading = false,
                        connectionStatus = when {
                            list.isNotEmpty() -> ConnectionStatus.Online
                            else -> ConnectionStatus.OfflineNoData
                        }
                    )
                }
            }
        }
    }
}