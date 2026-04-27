package com.pontini.food.truck_observability.impl

import com.pontini.food.truck_observability.domain.*
import com.pontini.food.truck_observability.provider.*

class TelemetryFoodTruckServiceImpl(
    private val logProvider: LogProvider?,
    private val eventProvider: EventProvider?,
    private val metricProvider: MetricProvider?,
    private val errorProvider: ErrorProvider?,
    private val flags: TelemetryFlags
) : TelemetryFoodTruckService {

    override fun log(message: String, params: Map<String, Any?>) {
        if (flags.enableLogs) logProvider?.log(message, params)
    }

    override fun track(event: Telemetry) {
        if (flags.enableEvents) eventProvider?.track(event)
    }

    override fun metric(name: String, value: Double) {
        if (flags.enableMetrics) metricProvider?.record(name, value)
    }

    override fun error(throwable: Throwable) {
        if (flags.enableErrors) errorProvider?.recordError(throwable)
    }
}
