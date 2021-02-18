package com.kries.autotool

import android.app.Application
import com.kries.autotool.autojs.AutoJs
import com.stardust.app.GlobalAppContext

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalAppContext.set(this)
        init()
    }

    private fun init() {
        AutoJs.initInstance(this)
    }

    companion object {
        private val TAG = "App"
    }
}