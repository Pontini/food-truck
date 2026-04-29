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
import com.pontini.food.observability.ObservabilityFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class ChatManagerImpl(
    private val context: Context,
    private val chatRepository: ChatRepository,
    private val observability: ObservabilityFacade
) : ChatManager, DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val isConnected = AtomicBoolean(false)

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            observability.event("chat_network_available")

            observability.log(
                "Network available - triggering reconnect",
                mapOf(
                    "action" to "connect",
                    "previous_state" to isConnected.get()
                )
            )

            connect()
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        observability.event("chat_manager_initialized")

        observability.log(
            "ChatManager initialized",
            mapOf(
                "thread" to Thread.currentThread().name
            )
        )
    }

    override fun onStart(owner: LifecycleOwner) {
        observability.event("chat_foreground")

        observability.log(
            "App moved to foreground",
            mapOf(
                "action" to "register_network_callback",
                "thread" to Thread.currentThread().name
            )
        )

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        connect()
    }

    override fun onStop(owner: LifecycleOwner) {
        observability.event("chat_background")

        observability.log(
            "App moved to background",
            mapOf(
                "action" to "cleanup",
                "cancel_scope" to true
            )
        )

        connectivityManager.unregisterNetworkCallback(networkCallback)

        scope.coroutineContext.cancelChildren()

        isConnected.set(false)
    }

    private fun connect() {
        val didAcquire = isConnected.compareAndSet(false, true)

        if (!didAcquire) {
            observability.log(
                "Connect skipped",
                mapOf(
                    "reason" to "already_connected",
                    "state" to true
                )
            )
            return
        }

        observability.log(
            "Starting chat connection",
            mapOf(
                "thread" to Thread.currentThread().name
            )
        )

        scope.launch {
            try {
                chatRepository.connect()

                observability.log(
                    "Chat connection established",
                    mapOf(
                        "state" to "connected"
                    )
                )

            } catch (e: Exception) {
                isConnected.set(false)

                observability.error(
                    e,
                    mapOf(
                        "stage" to "connect",
                        "action" to "reset_connection_flag"
                    )
                )
            }
        }
    }

    override suspend fun sendMessage(message: String, conversationId: String) {
        observability.log(
            "Sending message",
            mapOf(
                "conversationId" to conversationId,
                "message_size" to message.length
            )
        )

        chatRepository.sendMessage(message, conversationId)
    }

    override fun getMessagesById(conversationId: String): Flow<List<Message>> {
        observability.log(
            "Start observing messages",
            mapOf(
                "conversationId" to conversationId
            )
        )
        return chatRepository.getMessagesById(conversationId)
    }

    override fun getConnectionStatus(): Flow<ConnectionState> {
        observability.log(
            "Start observing connection state"
        )
        return chatRepository.observeConnection()
    }
}