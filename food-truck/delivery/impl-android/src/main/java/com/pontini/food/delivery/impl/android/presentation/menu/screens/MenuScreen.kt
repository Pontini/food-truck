package com.pontini.food.delivery.impl.android.presentation.menu.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pontini.food.delivery.domain.models.CartItem
import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.impl.android.R
import com.pontini.food.delivery.impl.android.presentation.menu.formatPrice
import com.pontini.food.delivery.impl.android.presentation.menu.viewmodel.MenuIntent
import com.pontini.food.delivery.impl.android.presentation.menu.viewmodel.MenuState
import com.pontini.food.delivery.impl.android.presentation.menu.viewmodel.MenuViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel(),
    onOrderPlaced: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.dispatcher(MenuIntent.Init)
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(state.placedOrderId) {
        state.placedOrderId?.let { onOrderPlaced(it) }
    }

    MenuScreenContent(
        state = state,
        onAdd = { item -> viewModel.dispatcher(MenuIntent.AddToCart(item)) },
        onRemove = { item -> viewModel.dispatcher(MenuIntent.RemoveFromCart(item)) },
        onCheckout = { viewModel.dispatcher(MenuIntent.PlaceOrder) }
    )
}

@Composable
fun MenuScreenContent(
    state: MenuState,
    onAdd: (MenuItem) -> Unit,
    onRemove: (MenuItem) -> Unit,
    onCheckout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(tonalElevation = 3.dp) {
            Text(
                text = stringResource(R.string.delivery_menu_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (state.isLoading && state.menu.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.menu, key = { it.id }) { item ->
                        val quantity = state.cart.find { it.menuItem.id == item.id }?.quantity ?: 0

                        MenuItemRow(
                            item = item,
                            quantity = quantity,
                            onAdd = { onAdd(item) },
                            onRemove = { onRemove(item) }
                        )
                    }
                }
            }
        }

        if (state.cart.isNotEmpty()) {
            CartSummaryBar(
                itemCount = state.itemCount,
                total = state.total,
                isPlacingOrder = state.isPlacingOrder,
                onCheckout = onCheckout
            )
        }
    }
}

@Composable
fun CartSummaryBar(
    itemCount: Int,
    total: Double,
    isPlacingOrder: Boolean,
    onCheckout: () -> Unit
) {
    Surface(tonalElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.delivery_cart_item_count, itemCount),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = formatPrice(total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onCheckout,
                enabled = !isPlacingOrder
            ) {
                if (isPlacingOrder) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text(stringResource(R.string.delivery_checkout_button))
            }
        }
    }
}

private val PREVIEW_MENU = listOf(
    MenuItem(
        id = "1",
        name = "X-Burger",
        description = "Hambúrguer, queijo, alface e tomate",
        price = 24.90,
        emoji = "🍔"
    ),
    MenuItem(
        id = "2",
        name = "Batata Frita",
        description = "Porção de batata frita crocante",
        price = 14.90,
        emoji = "🍟"
    ),
    MenuItem(
        id = "3",
        name = "Refrigerante",
        description = "Lata 350ml",
        price = 6.90,
        emoji = "🥤"
    )
)

private class MenuStatePreviewProvider : PreviewParameterProvider<MenuState> {
    override val values = sequenceOf(
        MenuState(isLoading = true),
        MenuState(menu = PREVIEW_MENU),
        MenuState(
            menu = PREVIEW_MENU,
            cart = listOf(
                CartItem(menuItem = PREVIEW_MENU[0], quantity = 2),
                CartItem(menuItem = PREVIEW_MENU[2], quantity = 1)
            )
        ),
        MenuState(
            menu = PREVIEW_MENU,
            cart = listOf(CartItem(menuItem = PREVIEW_MENU[0], quantity = 1)),
            isPlacingOrder = true
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenPreview(
    @PreviewParameter(MenuStatePreviewProvider::class) state: MenuState
) {
    MaterialTheme {
        MenuScreenContent(
            state = state,
            onAdd = {},
            onRemove = {},
            onCheckout = {}
        )
    }
}
