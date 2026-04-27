package com.pontini.food.truck_observability.domain

interface TelemetryFoodTruckService {
    fun log(message: String, params: Map<String, Any?> = emptyMap())
    fun track(event: Telemetry)
    fun metric(name: String, value: Double)
    fun error(throwable: Throwable)
}
