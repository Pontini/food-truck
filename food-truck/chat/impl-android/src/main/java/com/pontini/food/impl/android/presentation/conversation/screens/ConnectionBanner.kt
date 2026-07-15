package com.pontini.food.impl.android.presentation.conversation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.food.truck.impl.android.R
import com.pontini.food.impl.android.presentation.conversation.viewmodel.ConnectionStatus

@Composable
fun ConnectionBanner(status: ConnectionStatus) {

    val (background, text, textColor) = when (status) {

        is ConnectionStatus.Online -> Triple(
            Color(0xFF4CAF50),
            stringResource(R.string.status_online),
            Color.White
        )

        is ConnectionStatus.OfflineWithCache -> Triple(
            Color.LightGray,
            stringResource(R.string.connection_status_offline_cached),
            Color.Black
        )

        is ConnectionStatus.OfflineNoData -> Triple(
            Color(0xFFF44336),
            stringResource(R.string.connection_status_no_connection),
            Color.White
        )

        ConnectionStatus.Connecting ->  Triple(
            Color(0xFFF44336),
            stringResource(R.string.connection_status_no_connection),
            Color.White
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor)
    }
}