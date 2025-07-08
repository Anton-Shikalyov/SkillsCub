package com.lab42.skillsclub

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application(), DefaultLifecycleObserver {

}