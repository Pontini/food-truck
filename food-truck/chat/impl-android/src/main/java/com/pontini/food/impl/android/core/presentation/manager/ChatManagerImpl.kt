package com.pontini.food.impl.android.core.presentation.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.pontini.food.android.manager.ChatManager
import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.domain.repositories.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class ChatManagerImpl(
    private val context: Context,
    private val chatRepository: ChatRepository
) : ChatManager, DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val isConnected = AtomicBoolean(false)

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connect()
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        connect()
    }

    override fun onStop(owner: LifecycleOwner) {
        connectivityManager.unregisterNetworkCallback(networkCallback)

        scope.coroutineContext.cancelChildren()

        isConnected.set(false)
    }

    private fun connect() {
        if (isConnected.compareAndSet(false, true).not()) return

        scope.launch {
            try {
                chatRepository.connect()
            } catch (e: Exception) {
                e.printStackTrace()
                isConnected.set(false)
            }
        }
    }

    override suspend fun sendMessage(message: String, conversationId: String) {
        chatRepository.sendMessage(message, conversationId)
    }

    override fun getMessagesById(conversationId: String): Flow<List<Message>> {
        return chatRepository.getMessagesById(conversationId)
    }

    override fun getConnectionStatus(): Flow<ConnectionState> {
        return chatRepository.observeConnection()
    }
}