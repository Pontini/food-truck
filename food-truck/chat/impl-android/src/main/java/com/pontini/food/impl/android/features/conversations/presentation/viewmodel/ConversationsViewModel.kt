package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import com.pontini.food.impl.domain.repoistories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val repository: ConversationRepository
) : BaseViewModel<ConversationsIntent, ConversationsState>(ConversationsState()) {

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true, error = null) }
            try {
                val result = repository.getLastMessages()
                setState {
                    it.copy(
                        conversations = result,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                Log.e("ConversationsViewModel", "Error fetching conversations", e)
                setState {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar"
                    )
                }
            }
        }
    }

    override fun dispatcher(intent: ConversationsIntent) {}
}