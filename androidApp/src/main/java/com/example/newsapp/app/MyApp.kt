package com.example.newsapp.app

import android.app.Application
import com.example.shared.koin.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MyApp)
        }
    }
}