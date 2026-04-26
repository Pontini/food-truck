package com.pontini.food.impl.android.conversations.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.pontini.food.domain.model.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pontini.food.domain.model.TypeMessage
import com.pontini.food.impl.android.features.chat.presentation.screens.Avatar


@Composable
fun ChatBubble(message: Message) {

    val isMe = message.typeMessage == TypeMessage.SENT

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        if (!isMe) {
            Avatar(message.senderName)
            Spacer(modifier = Modifier.width(6.dp))
        }

        Box(
            modifier = Modifier
                .background(
                    color = if (isMe)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                text = message.text,
                color = if (isMe)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isMe) {
            Spacer(modifier = Modifier.width(6.dp))
            Avatar("Me")
        }
    }
}