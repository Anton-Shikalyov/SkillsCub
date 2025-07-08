package com.lab42.skillsclub.di

import android.content.Context
import androidx.room.Room
import com.lab42.skillsclub.DB_Space
import com.lab42.skillsclub.data.data_base.AppDB
import com.lab42.skillsclub.data.data_base.DAO.AppUsageDao
import com.lab42.skillsclub.data.data_base.DAO.HelloDao
import com.lab42.skillsclub.data.data_base.DAO.ProfileDao
import com.lab42.skillsclub.domain.use_cases.mapper.AppUsageMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.AppUsageMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloConfigMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloConfigMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloStartNotificationInterface
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloStartNotificationMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.ProfileMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.ProfileMapInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun provideAppUsageMapInterface(): AppUsageMapInterface {
        return AppUsageMapImpl()
    }
    @Provides
    fun provideProfileMapInterface(): ProfileMapInterface {
        return ProfileMapImpl()
    }

    @Provides
    fun provideHelloConfigMapInterface(): HelloConfigMapInterface {
        return HelloConfigMapImpl()
    }

    @Provides
    fun provideHelloStartNotificationMapInterface(): HelloStartNotificationInterface {
        return HelloStartNotificationMapImpl()
    }

    @Provides
    @Singleton
    fun provideAppDB(context: Context): AppDB {
        return  Room.databaseBuilder(
            context.applicationContext,
            AppDB::class.java,
            DB_Space.DB_NAME
        ).build()
            // fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAppUsageDao(appDB: AppDB): AppUsageDao {
        return appDB.getAppUsageDao()
    }

    @Provides
    @Singleton
    fun provideProfileDao(appDB: AppDB): ProfileDao {
        return appDB.getProfileDao()
    }

    @Provides
    @Singleton
    fun provideHelloDao(appDB: AppDB): HelloDao {
        return appDB.getHelloDao()
    }

}