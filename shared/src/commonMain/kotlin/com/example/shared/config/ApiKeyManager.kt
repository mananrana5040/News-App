package com.example.shared.config

import com.example.newsapp.BuildKonfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlin.time.Duration.Companion.seconds

class ApiKeyManager {
    private var availableKeys: List<String> = emptyList()
    private var currentKeyIndex = 0

    suspend fun initializeRemoteKeys() {
        try {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.settings {
                minimumFetchInterval = 0.seconds
                fetchTimeout = 60.seconds
            }
            remoteConfig.fetchAndActivate()
            val keysString = remoteConfig.getValue("news_api_keys").asString()
            availableKeys = if (keysString.isEmpty()){
                listOf(BuildKonfig.API_KEY)
            }else{
                keysString.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
            }
            currentKeyIndex = 0
            println("available: $availableKeys")
            println("available: Current: ${availableKeys[currentKeyIndex]}")
        } catch (e: Exception) {

        }
    }

    fun getActiveKey(): String = availableKeys.getOrElse(currentKeyIndex) {BuildKonfig.API_KEY }

    fun rotateKey(): Boolean {
        if (currentKeyIndex < availableKeys.size - 1) {
            currentKeyIndex++
            return true
        }
        return false
    }

}