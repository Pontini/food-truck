package com.pontini.food.delivery.domain.models

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val emoji: String
)
