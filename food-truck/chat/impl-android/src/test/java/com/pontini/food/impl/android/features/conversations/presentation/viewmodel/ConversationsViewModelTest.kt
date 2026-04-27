package com.pontini.food.impl.android.features.conversations.presentation.viewmodel

import app.cash.turbine.test
import com.pontini.food.impl.features.conversations.domain.model.*
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversationsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ConversationRepository
    private lateinit var viewModel: ConversationsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when init should emit loading then success with offlineWithCache status (current behavior)`() = runTest {
        val conversations = listOf(
            Conversation(
                id = "1",
                name = "Chat Test",
                lastMessage = "Hello",
                timestamp = 123L
            )
        )

        coEvery { repository.getConversations() } returns flow {
            emit(ConversationResult.Loading)
            emit(
                ConversationResult.Success(
                    data = conversations,
                    source = Source.REMOTE
                )
            )
        }

        viewModel = ConversationsViewModel(repository)

        viewModel.state.test {
            viewModel.dispatcher(ConversationsIntent.Init)
            advanceUntilIdle()

            // 🔹 Estado inicial
            val initial = awaitItem()
            assertEquals(false, initial.isLoading)

            // 🔹 Loading
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            // 🔹 Success (⚠️ comportamento atual baseado no state anterior)
            val success = awaitItem()
            assertEquals(false, success.isLoading)
            assertEquals(conversations, success.conversations)
            assertEquals(ConnectionStatus.OfflineWithCache, success.connectionStatus)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when error and no cache should set offline no data`() = runTest {
        coEvery { repository.getConversations() } returns flow {
            emit(ConversationResult.Error("Erro"))
        }

        viewModel = ConversationsViewModel(repository)

        viewModel.state.test {
            viewModel.dispatcher(ConversationsIntent.Init)
            advanceUntilIdle()

            // 🔹 Estado inicial
            awaitItem()

            // 🔹 Error
            val state = awaitItem()

            assertEquals(false, state.isLoading)
            assertEquals(ConnectionStatus.OfflineNoData, state.connectionStatus)
            assertEquals("Erro", state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when has cache then error should keep offline with cache`() = runTest {
        val conversations = listOf(
            Conversation(
                id = "1",
                name = "Cached Chat",
                lastMessage = "Oi",
                timestamp = 123L
            )
        )

        coEvery { repository.getConversations() } returns flow {
            emit(
                ConversationResult.Success(
                    data = conversations,
                    source = Source.CACHE
                )
            )
            emit(ConversationResult.Error("Erro"))
        }

        viewModel = ConversationsViewModel(repository)

        viewModel.state.test {
            viewModel.dispatcher(ConversationsIntent.Init)
            advanceUntilIdle()

            // 🔹 Estado inicial
            awaitItem()

            // 🔹 Success (cache)
            val success = awaitItem()
            assertEquals(conversations, success.conversations)

            // 🔹 Error mantendo cache
            val error = awaitItem()
            assertEquals(ConnectionStatus.OfflineWithCache, error.connectionStatus)
            assertEquals("Erro", error.error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}