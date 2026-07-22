package com.pontini.food.delivery.impl.android.presentation.order.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pontini.food.delivery.domain.models.OrderStatus
import com.pontini.food.delivery.impl.android.R
import com.pontini.food.delivery.impl.android.presentation.order.viewmodel.OrderStatusIntent
import com.pontini.food.delivery.impl.android.presentation.order.viewmodel.OrderStatusState
import com.pontini.food.delivery.impl.android.presentation.order.viewmodel.OrderStatusViewModel
import org.koin.androidx.compose.koinViewModel

private val STEPS = listOf(
    OrderStatus.RECEIVED,
    OrderStatus.PREPARING,
    OrderStatus.ON_THE_WAY,
    OrderStatus.DELIVERED
)

@Composable
fun OrderStatusScreen(
    orderId: String,
    viewModel: OrderStatusViewModel = koinViewModel(),
    onBackToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.dispatcher(OrderStatusIntent.Init(orderId))
    }

    OrderStatusScreenContent(
        state = state,
        onBackToHome = onBackToHome
    )
}

@Composable
fun OrderStatusScreenContent(
    state: OrderStatusState,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.delivery_order_confirmed_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.delivery_order_id_label, state.orderId.take(8)),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        STEPS.forEachIndexed { index, step ->
            val isCompleted = STEPS.indexOf(state.status) >= index

            OrderStatusStep(
                label = stepLabel(step),
                isCompleted = isCompleted,
                isLast = index == STEPS.lastIndex
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(visible = state.status == OrderStatus.DELIVERED) {
            Button(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delivery_back_home_button))
            }
        }
    }
}

@Composable
private fun OrderStatusStep(
    label: String,
    isCompleted: Boolean,
    isLast: Boolean
) {
    val color = if (isCompleted) MaterialTheme.colorScheme.primary else Color.LightGray

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color, CircleShape)
            )

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(24.dp)
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
            color = if (isCompleted) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
    }
}

@Composable
private fun stepLabel(status: OrderStatus): String = when (status) {
    OrderStatus.RECEIVED -> stringResource(R.string.delivery_status_received)
    OrderStatus.PREPARING -> stringResource(R.string.delivery_status_preparing)
    OrderStatus.ON_THE_WAY -> stringResource(R.string.delivery_status_on_the_way)
    OrderStatus.DELIVERED -> stringResource(R.string.delivery_status_delivered)
}

private class OrderStatusPreviewProvider : PreviewParameterProvider<OrderStatusState> {
    override val values = sequenceOf(
        OrderStatusState(orderId = "abcd1234", status = OrderStatus.RECEIVED),
        OrderStatusState(orderId = "abcd1234", status = OrderStatus.PREPARING),
        OrderStatusState(orderId = "abcd1234", status = OrderStatus.ON_THE_WAY),
        OrderStatusState(orderId = "abcd1234", status = OrderStatus.DELIVERED)
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderStatusScreenPreview(
    @PreviewParameter(OrderStatusPreviewProvider::class) state: OrderStatusState
) {
    MaterialTheme {
        OrderStatusScreenContent(
            state = state,
            onBackToHome = {}
        )
    }
}
