package com.pontini.food.impl.android.features.conversations.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pontini.food.impl.features.conversations.domain.model.Conversation

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) {
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = conversation.name.firstOrNull()?.uppercase() ?: "?",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = conversation.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = conversation.lastMessage,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Text(
            text = formatTime(conversation.timestamp),
            color = Color.Gray,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm")
    return sdf.format(java.util.Date(timestamp))
}