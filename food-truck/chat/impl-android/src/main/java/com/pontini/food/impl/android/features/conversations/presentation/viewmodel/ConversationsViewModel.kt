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

                when (result) {

                    is ConversationResult.Loading -> {
                        setState {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is ConversationResult.Success -> {
                        setState { state ->
                            val newStatus = when (result.source) {
                                Source.REMOTE -> ConnectionStatus.Online
                                Source.CACHE -> when {
                                    state.connectionStatus == ConnectionStatus.Online ->
                                        ConnectionStatus.Online
                                    result.data.isNotEmpty() ->
                                        ConnectionStatus.OfflineWithCache
                                    else ->
                                        ConnectionStatus.OfflineNoData
                                }
                            }

                            state.copy(
                                conversations = result.data,
                                isLoading = false,
                                connectionStatus = newStatus,
                                error = null
                            )
                        }
                    }

                    is ConversationResult.Error -> {
                        setState { state ->
                            state.copy(
                                isLoading = false,
                                connectionStatus = if (state.conversations.isNotEmpty()) {
                                    ConnectionStatus.OfflineWithCache
                                } else {
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