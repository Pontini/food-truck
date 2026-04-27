package com.pontini.food.truck_observability.domain

data class TelemetryFlags(
    val enableLogs: Boolean = true,
    val enableEvents: Boolean = true,
    val enableMetrics: Boolean = true,
    val enableErrors: Boolean = true
)
