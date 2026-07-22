package com.pontini.food.delivery.impl.android.presentation.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.impl.android.R
import com.pontini.food.delivery.impl.android.presentation.menu.formatPrice

@Composable
fun MenuItemRow(
    item: MenuItem,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.emoji, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Bold)
            Text(
                text = item.description,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            Text(
                text = formatPrice(item.price),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (quantity == 0) {
            Button(onClick = onAdd) {
                Text(stringResource(R.string.delivery_add_button))
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onRemove) {
                    Text("-")
                }

                Text(
                    text = quantity.toString(),
                    modifier = Modifier
                        .width(32.dp)
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center
                )

                OutlinedButton(onClick = onAdd) {
                    Text("+")
                }
            }
        }
    }
}
