package com.pontini.food.truck_observability


import com.pontini.food.truck_observability.domain.TelemetryFlags
import com.pontini.food.truck_observability.domain.TelemetryFoodTruckService
import com.pontini.food.truck_observability.impl.TelemetryFoodTruckServiceImpl
import com.pontini.food.truck_observability.provider.impl.*

object TelemetryFoodTruckInitializer {

    fun create(): TelemetryFoodTruckService {
        return TelemetryFoodTruckServiceImpl(
            logProvider = DefaultLogProvider(),
            eventProvider = DefaultEventProvider(),
            metricProvider = DefaultMetricProvider(),
            errorProvider = DefaultErrorProvider(),
            flags = TelemetryFlags(
                enableLogs = true,
                enableEvents = true,
                enableMetrics = true,
                enableErrors = true
            )
        )
    }
}