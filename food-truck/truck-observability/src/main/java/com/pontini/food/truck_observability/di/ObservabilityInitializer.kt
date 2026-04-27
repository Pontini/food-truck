package com.pontini.food.truck_observability.di

import com.pontini.food.truck_observability.domain.*
import com.pontini.food.truck_observability.impl.TelemetryFoodTruckServiceImpl
import com.pontini.food.truck_observability.provider.ErrorProvider
import com.pontini.food.truck_observability.provider.EventProvider
import com.pontini.food.truck_observability.provider.LogProvider
import com.pontini.food.truck_observability.provider.MetricProvider
import com.pontini.food.truck_observability.provider.impl.*
import org.koin.dsl.module


val telemetryFoodTruckServiceModule = module {
    single<LogProvider> { DefaultLogProvider() }
    single<EventProvider> { DefaultEventProvider() }
    single<MetricProvider> { DefaultMetricProvider() }
    single<ErrorProvider> { DefaultErrorProvider() }

    single { TelemetryFlags() }

    single<TelemetryFoodTruckService> {
        TelemetryFoodTruckServiceImpl(
            logProvider = get(),
            eventProvider = get(),
            metricProvider = get(),
            errorProvider = get(),
            flags = get()
        )
    }

}