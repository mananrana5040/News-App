package com.example.shared.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class CreateDataStore {
    fun create(): DataStore<Preferences>
}