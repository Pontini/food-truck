package com.pontini.food.impl.android.features.conversations.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConnectionStatus

@Composable
fun ConnectionBanner(status: ConnectionStatus) {

    val (background, text, textColor) = when (status) {

        is ConnectionStatus.Online -> Triple(
            Color(0xFF4CAF50),
            "Online",
            Color.White
        )

        is ConnectionStatus.OfflineWithCache -> Triple(
            Color.LightGray,
            "Dados offline",
            Color.Black
        )

        is ConnectionStatus.OfflineNoData -> Triple(
            Color(0xFFF44336),
            "Sem conexão",
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