package com.pontini.food.impl.android.features.chat.presentation.viewmodel

import com.pontini.food.android.manager.ChatManager
import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.domain.model.TypeMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val chatManager: ChatManager = mockk(relaxed = true)

    private lateinit var viewModel: ChatViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(chatManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update messages when receiving messages`() = runTest {
        val conversationId = "123"
        val messagesFlow = MutableSharedFlow<List<Message>>(replay = 1)

        coEvery { chatManager.getMessagesById(conversationId) } returns messagesFlow
        coEvery { chatManager.getConnection() } returns emptyFlow()

        viewModel.dispatcher(ChatIntent.Init(conversationId))
        runCurrent()

        val fakeMessages = listOf(
            Message(
                id = "1",
                text = "Hello",
                senderName = "User",
                conversationId = conversationId,
                timestamp = 0L,
                typeMessage = TypeMessage.RECEIVED
            )
        )

        messagesFlow.emit(fakeMessages)
        runCurrent()

        val state = viewModel.state.value
        assertEquals(1, state.messages.size)
        assertEquals("Hello", state.messages.first().text)
    }

    @Test
    fun `should update connection state to connected`() = runTest {
        val conversationId = "123"
        val connectionFlow = MutableSharedFlow<ConnectionState>(replay = 1)

        coEvery { chatManager.getMessagesById(conversationId) } returns emptyFlow()
        coEvery { chatManager.getConnection() } returns connectionFlow

        viewModel.dispatcher(ChatIntent.Init(conversationId))
        runCurrent()

        connectionFlow.emit(ConnectionState.Connection.Connected)
        runCurrent()

        val state = viewModel.state.value
        assertFalse(state.isConnecting)
        assertTrue(state.isConnected)
    }

    @Test
    fun `should update error when connection fails`() = runTest {
        val conversationId = "123"
        val connectionFlow = MutableSharedFlow<ConnectionState>(replay = 1)

        coEvery { chatManager.getMessagesById(conversationId) } returns emptyFlow()
        coEvery { chatManager.getConnection() } returns connectionFlow

        viewModel.dispatcher(ChatIntent.Init(conversationId))
        runCurrent()

        connectionFlow.emit(ConnectionState.Connection.FailedConnected("Falha"))
        runCurrent()

        val state = viewModel.state.value

        assertEquals("Falha", state.error)
        assertFalse(state.isConnecting)
    }

    @Test
    fun `should send message`() = runTest {
        val conversationId = "123"

        coEvery { chatManager.getMessagesById(conversationId) } returns emptyFlow()
        coEvery { chatManager.getConnection() } returns emptyFlow()

        viewModel.dispatcher(ChatIntent.Init(conversationId))
        runCurrent()

        viewModel.dispatcher(ChatIntent.SendMessage("Oi"))
        runCurrent()

        coVerify {
            chatManager.sendMessage("Oi", conversationId)
        }
    }
}