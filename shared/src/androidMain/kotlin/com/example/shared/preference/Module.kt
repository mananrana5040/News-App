package com.example.shared.preference

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single { CreateDataStore(androidContext()).create() }
}