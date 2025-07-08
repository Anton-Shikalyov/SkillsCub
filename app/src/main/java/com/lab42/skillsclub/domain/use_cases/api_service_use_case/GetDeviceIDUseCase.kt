package com.lab42.skillsclub.domain.use_cases.api_service_use_case

import com.lab42.skillsclub.domain.impl.DeviceIdProvider
import javax.inject.Inject

class GetDeviceIDUseCase @Inject constructor(
    private val deviceIdProvider: DeviceIdProvider
) {
    fun execute(): String {
        return deviceIdProvider.getDeviceId()
    }
}