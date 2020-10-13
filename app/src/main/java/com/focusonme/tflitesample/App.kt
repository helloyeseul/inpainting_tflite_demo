package com.focusonme.tflitesample

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class App : Application() {
    companion object {
        lateinit var context: Context
    }

    init {
        System.loadLibrary("opencv_java4")
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Logger.addLogAdapter(AndroidLogAdapter())
    }

}