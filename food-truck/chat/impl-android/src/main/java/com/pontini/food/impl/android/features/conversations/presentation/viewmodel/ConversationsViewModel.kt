package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import com.pontini.food.impl.features.conversations.domain.model.ConversationResult
import com.pontini.food.impl.features.conversations.domain.model.Source
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val repository: ConversationRepository
) : BaseViewModel<ConversationsIntent, ConversationsState>(ConversationsState()) {

    override fun dispatcher(intent: ConversationsIntent) {
        when (intent) {
            ConversationsIntent.Init -> onInit()
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            repository.getConversations().collect { result ->
                setState { state ->
                    when (result) {
                        is ConversationResult.Loading -> {
                            state.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                        is ConversationResult.Success -> {
                            val hasData = result.data.isNotEmpty()

                            val connectionStatus = when {
                                hasData && !state.isLoading -> ConnectionStatus.Online
                                hasData -> ConnectionStatus.OfflineWithCache
                                else -> ConnectionStatus.OfflineNoData
                            }

                            state.copy(
                                conversations = result.data,
                                isLoading = false,
                                connectionStatus = connectionStatus,
                                error = null
                            )
                        }
                        is ConversationResult.Error -> {
                            state.copy(
                                isLoading = false,
                                connectionStatus = when {
                                    state.conversations.isNotEmpty() ->
                                        ConnectionStatus.OfflineWithCache
                                    else ->
                                        ConnectionStatus.OfflineNoData
                                },
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}