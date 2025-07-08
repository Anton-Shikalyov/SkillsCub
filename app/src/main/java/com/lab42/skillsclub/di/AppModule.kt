package com.lab42.skillsclub.di

import android.app.Application
import android.content.Context
import com.lab42.skillsclub.data.SharedPrefImpl
import com.lab42.skillsclub.data.SharedPrefInterface
import com.lab42.skillsclub.domain.impl.DeviceIdProvider
import com.lab42.skillsclub.domain.impl.DeviceIdProviderImpl
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetDeviceIDUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(context: Context): SharedPrefInterface {
        return SharedPrefImpl(context)
    }
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    fun provideGetDeviceIDUseCase(deviceIdProvider: DeviceIdProvider): GetDeviceIDUseCase {
        return GetDeviceIDUseCase(deviceIdProvider)
    }
    @Provides
    fun provideDeviceIdProvider(context: Context): DeviceIdProvider {
        return DeviceIdProviderImpl(context)
    }

}