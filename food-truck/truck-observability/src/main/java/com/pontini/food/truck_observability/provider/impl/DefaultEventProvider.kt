package com.pontini.food.truck_observability.provider.impl

import com.pontini.food.truck_observability.domain.Telemetry
import com.pontini.food.truck_observability.provider.EventProvider

class DefaultEventProvider : EventProvider {
    override fun track(event: Telemetry) {
        println("Event: ${event.name} -> ${event.params}")
    }
}
