package com.example.shared.koin

import com.example.shared.api.NewsApiService
import com.example.shared.preference.ThemeManager
import com.example.shared.repository.NewsRepository
import com.example.shared.viewmodel.NewsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    single { NewsApiService(get()) }

    single { NewsRepository(get()) }

    factory { NewsViewModel(get()) }

    single { ThemeManager(get()) }
}
