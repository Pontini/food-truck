package com.pontini.food.foodtruck.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pontini.food.android.navigate.ChatNavigator
import com.pontini.food.foodtruck.presentation.navigation.AppNavHost
import com.pontini.food.foodtruck.presentation.themes.FoodTruckTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val chatNavigator: ChatNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodTruckTheme {
                AppNavHost(chatNavigator)
            }
        }
    }
}