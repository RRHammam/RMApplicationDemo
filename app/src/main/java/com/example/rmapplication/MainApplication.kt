package com.example.rmapplication

import android.app.Application
import android.content.Context

class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }


    companion object {
        var instance = MainApplication()
        fun getContext(): Context{
            return instance.applicationContext
        }
    }

}
