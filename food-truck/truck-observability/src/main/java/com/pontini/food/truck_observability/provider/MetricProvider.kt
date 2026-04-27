package com.pontini.food.truck_observability.provider

interface MetricProvider {
    fun record(name: String, value: Double)
}
