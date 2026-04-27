package com.pontini.food.truck_observability.provider.impl

import com.pontini.food.truck_observability.provider.ErrorProvider

class DefaultErrorProvider : ErrorProvider {
    override fun recordError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
