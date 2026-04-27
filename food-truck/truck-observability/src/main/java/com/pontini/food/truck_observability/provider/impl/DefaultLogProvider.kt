package com.pontini.food.truck_observability.provider.impl

import android.util.Log
import com.pontini.food.truck_observability.provider.LogProvider

class DefaultLogProvider : LogProvider {
    override fun log(message: String, params: Map<String, Any?>) {
        Log.d("Telemetry", "$message -> $params")
    }
}
