package com.pontini.food.impl.android.presentation.conversation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.domain.repositories.ConversationRepository
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.catch
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
        println("LOADING CONVERSATIONS")
        viewModelScope.launch {
            setState { state ->
                state.copy(
                    isLoading = true,
                    error = null
                )
            }
            repository.getConversations().catch {
                println("LOADING CONVERSATIONS ERROR "+it.message)

                setState { state ->
                    state.copy(
                        isLoading = false,
                        connectionStatus = when {
                            state.conversations.isNotEmpty() ->
                                ConnectionStatus.OfflineWithCache

                            else ->
                                ConnectionStatus.OfflineNoData
                        },
                        error = it.message
                    )
                }
            }.collect { result ->
                println("LOADING CONVERSATIONS RESULT "+result.source)

                val hasData = result.data.isNotEmpty()
                setState { state ->
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
            }
        }
    }
}