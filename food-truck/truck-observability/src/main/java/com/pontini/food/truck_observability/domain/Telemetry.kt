package com.pontini.food.truck_observability.domain

data class Telemetry(
    val name: String,
    val params: Map<String, Any?> = emptyMap()
)
