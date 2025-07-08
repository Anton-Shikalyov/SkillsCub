package com.lab42.skillsclub.domain.impl

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import javax.inject.Inject

interface DeviceIdProvider {
    fun getDeviceId(): String
}
class DeviceIdProviderImpl @Inject constructor(
    private val context: Context
) : DeviceIdProvider {
    @SuppressLint("HardwareIds")
    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}