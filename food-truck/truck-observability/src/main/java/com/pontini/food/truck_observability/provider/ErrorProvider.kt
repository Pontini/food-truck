package com.pontini.food.truck_observability.provider

interface ErrorProvider {
    fun recordError(throwable: Throwable)
}
