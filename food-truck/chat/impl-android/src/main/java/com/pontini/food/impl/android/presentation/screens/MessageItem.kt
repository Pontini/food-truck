package com.pontini.food.impl.android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pontini.food.domain.model.Message

@Composable
fun MessageItem(message: Message) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.sender.first().uppercase(),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {

            Text(
                text = message.sender,
                fontWeight = FontWeight.Bold
            )

            Text(text = message.text)
        }
    }
}