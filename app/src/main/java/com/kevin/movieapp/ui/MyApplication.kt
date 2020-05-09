package com.kevin.movieapp.ui

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object{
        private var mInstance: MyApplication? = null

        @Synchronized
        fun getInstance(): MyApplication? {
            return mInstance
        }

        fun getContext(): Context? {
            return mInstance?.getApplicationContext()
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}