package com.example.instant_message

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

object AppLifecycleTracker: DefaultLifecycleObserver{
    var isAppInForeground = true
    private set

    fun init(application: Application){
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    override fun onStop(owner: LifecycleOwner) {
        isAppInForeground = false
        Log.d("AppLifecycleTracker", "应用进入后台")
    }
    override fun onStart(owner: LifecycleOwner) {
        isAppInForeground = true
        Log.d("AppLifecycleTracker", "应用进入前台")
    }
}