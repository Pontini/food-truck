package com.pontini.food.truck_observability.provider

import com.pontini.food.truck_observability.domain.Telemetry

interface EventProvider {
    fun track(event: Telemetry)
}
