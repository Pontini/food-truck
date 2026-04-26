package com.pontini.food.impl.android.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pontini.food.android.lifecycle.AppLifecycle
import com.pontini.food.android.lifecycle.AppLifecycleObserver

class AppLifecycleImpl(
    private val lifecycle: Lifecycle
) : AppLifecycle {

    private val map = mutableMapOf<AppLifecycleObserver, DefaultLifecycleObserver>()

    override fun addObserver(observer: AppLifecycleObserver) {
        val androidObserver = object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                observer.onStart()
            }

            override fun onStop(owner: LifecycleOwner) {
                observer.onStop()
            }
        }

        map[observer] = androidObserver
        lifecycle.addObserver(androidObserver)
    }

    override fun removeObserver(observer: AppLifecycleObserver) {
        val androidObserver = map.remove(observer)
        if (androidObserver != null) {
            lifecycle.removeObserver(androidObserver)
        }
    }
}