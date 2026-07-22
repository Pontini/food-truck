import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pontini.food.foodtruck.R
import com.pontini.food.foodtruck.presentation.themes.FoodTruckTheme
import com.food.truck.impl.android.R as ChatR
import com.pontini.food.delivery.impl.android.R as DeliveryR

data class HomeAction(
    @StringRes val titleRes: Int,
    val onClick: () -> Unit
)

private val HOME_BUTTON_COLORS = listOf(
    Color(0xFF4CAF50),
    Color(0xFF0D47A1),
    Color(0xFFFF6F00),
    Color(0xFF6A1B9A)
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    actions: List<HomeAction>
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFEAEAEA)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            actions.forEachIndexed { index, action ->
                OptionButton(
                    text = stringResource(action.titleRes),
                    color = HOME_BUTTON_COLORS[index % HOME_BUTTON_COLORS.size],
                    onClick = action.onClick
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FoodTruckTheme {
        HomeScreen(
            actions = listOf(
                HomeAction(titleRes = DeliveryR.string.delivery_home_button) {},
                HomeAction(titleRes = ChatR.string.chat_home_button) {}
            )
        )
    }
}

@Preview(name = "Sem features registradas", showBackground = true)
@Composable
private fun HomeScreenEmptyPreview() {
    FoodTruckTheme {
        HomeScreen(actions = emptyList())
    }
}
