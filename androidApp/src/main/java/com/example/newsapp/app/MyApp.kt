package com.example.newsapp.app

import android.app.Application
import com.example.shared.firebase.FirebaseInitializer
import com.example.shared.koin.commonModule
import com.example.shared.koin.databaseModule
import com.example.shared.preference.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseInitializer.initialize(this)
        startKoin {
            androidContext(this@MyApp)
            modules(commonModule, platformModule, databaseModule)
        }
    }
}