package com.pontini.food.impl.android.observability

import com.pontini.food.observability.ObservabilityFacade
import com.pontini.food.truck_observability.domain.Telemetry
import com.pontini.food.truck_observability.domain.TelemetryFoodTruckService

class ObservabilityFacadeImpl(
    private val telemetry: TelemetryFoodTruckService
) : ObservabilityFacade {

    override fun log(
        message: String,
        params: Map<String, Any?>
    ) {
        telemetry.log(message, params)
    }

    override fun event(
        name: String,
        params: Map<String, Any?>
    ) {
        telemetry.track(
            Telemetry(
                name = name,
                params = params
            )
        )
    }

    override fun metric(
        name: String,
        value: Double
    ) {
        telemetry.metric(name, value)
    }

    override fun error(
        throwable: Throwable,
        params: Map<String, Any?>
    ) {
        telemetry.log(
            message = "error",
            params = params + mapOf(
                "message" to throwable.message,
                "type" to throwable::class.simpleName
            )
        )

        telemetry.error(throwable)
    }
}