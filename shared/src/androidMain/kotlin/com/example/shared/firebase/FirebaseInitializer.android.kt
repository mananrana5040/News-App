package com.example.shared.firebase

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

actual object FirebaseInitializer {
    actual fun initialize(context: Any?) {
        val androidContext = context as? Context
        if (androidContext != null) {
            Firebase.initialize(androidContext)
        }
    }
}