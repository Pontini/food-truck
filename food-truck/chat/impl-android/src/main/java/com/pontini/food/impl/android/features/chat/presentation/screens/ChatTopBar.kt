package com.pontini.food.impl.android.conversations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime. Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    isConnected: Boolean,
    isConnecting: Boolean
) {
    val color = when {
        isConnecting -> Color.Yellow
        isConnected -> Color(0xFF4CAF50)
        else -> Color.Red
    }

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text("Atendimento")

                Text(
                    text = when {
                        isConnecting -> "Conectando..."
                        isConnected -> "Online"
                        else -> "Offline"
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
    )
}