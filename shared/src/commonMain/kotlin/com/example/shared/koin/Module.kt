package com.example.shared.koin

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.shared.api.NewsApiService
import com.example.shared.config.ApiKeyManager
import com.example.shared.database.AppDatabase
import com.example.shared.database.getDatabaseBuilder
import com.example.shared.preference.ThemeManager
import com.example.shared.repository.BookmarkRepository
import com.example.shared.repository.NewsRepository
import com.example.shared.viewmodel.AuthViewModel
import com.example.shared.viewmodel.BookmarkViewModel
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
            expectSuccess = true
        }
    }

    single { NewsApiService(get(), get()) }

    single { NewsRepository(get()) }

    single { ApiKeyManager() }

    factory { NewsViewModel(get()) }

    factory { AuthViewModel(get()) }

    single { ThemeManager(get()) }
}

val databaseModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<AppDatabase>().bookmarkDao() }
    single { BookmarkRepository(get()) }
    factory { BookmarkViewModel(get()) }

}
