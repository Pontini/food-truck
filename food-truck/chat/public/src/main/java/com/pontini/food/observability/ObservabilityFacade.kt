package com.pontini.food.observability


interface ObservabilityFacade {

    fun log(
        message: String,
        params: Map<String, Any?> = emptyMap()
    )

    fun event(
        name: String,
        params: Map<String, Any?> = emptyMap()
    )

    fun metric(
        name: String,
        value: Double
    )

    fun error(
        throwable: Throwable,
        params: Map<String, Any?> = emptyMap()
    )
}