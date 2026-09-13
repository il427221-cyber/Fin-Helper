package ru.myproject.finhelper.ui

import android.app.Application
import android.content.Context

class MockApplicationForPreview(baseContext: Context) : Application() {
    init {
        attachBaseContext(baseContext)
    }
}