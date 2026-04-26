package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import com.pontini.food.impl.domain.repoistories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val repository: ConversationRepository
) : BaseViewModel<ConversationsIntent, ConversationsState>(ConversationsState()) {

    init {
        observe()
        refresh()
    }

    private fun observe() {
        viewModelScope.launch {
            repository.observe().collect { list ->
                setState {
                    it.copy(
                        conversations = list,
                        connectionStatus = when {
                            it.connectionStatus is ConnectionStatus.Online -> ConnectionStatus.Online
                            list.isNotEmpty() -> ConnectionStatus.OfflineWithCache
                            else -> ConnectionStatus.OfflineNoData
                        }
                    )
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {

            setState { it.copy(isLoading = true, error = null) }

            try {
                repository.refresh()

                setState {
                    it.copy(
                        isLoading = false,
                        connectionStatus = ConnectionStatus.Online
                    )
                }

            } catch (e: Exception) {
                setState {
                    it.copy(
                        isLoading = false,
                        connectionStatus = if (it.conversations.isNotEmpty()) {
                            ConnectionStatus.OfflineWithCache
                        } else {
                            ConnectionStatus.OfflineNoData
                        },
                        error = "Sem conexão"
                    )
                }
            }
        }
    }

    override fun dispatcher(intent: ConversationsIntent) {

    }
}