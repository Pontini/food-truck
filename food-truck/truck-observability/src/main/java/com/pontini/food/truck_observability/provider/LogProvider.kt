package com.pontini.food.truck_observability.provider

interface LogProvider {
    fun log(message: String, params: Map<String, Any?> = emptyMap())
}
