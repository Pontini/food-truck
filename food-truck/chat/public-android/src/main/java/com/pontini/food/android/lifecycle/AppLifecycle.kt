package com.pontini.food.android.lifecycle

interface AppLifecycle {
    fun addObserver(observer: AppLifecycleObserver)
    fun removeObserver(observer: AppLifecycleObserver)
}

interface AppLifecycleObserver {
    fun onStart() {}
    fun onStop() {}
}