package com.pontini.food.truck_observability.provider.impl

import com.pontini.food.truck_observability.provider.MetricProvider

class DefaultMetricProvider : MetricProvider {
    override fun record(name: String, value: Double) {
        println("Metric: $name = $value")
    }
}
