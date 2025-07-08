package com.lab42.skillsclub.data.data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lab42.skillsclub.data.data_base.DAO.AppUsageDao
import com.lab42.skillsclub.data.data_base.DAO.HelloDao
import com.lab42.skillsclub.data.data_base.DAO.ProfileDao
import com.lab42.skillsclub.data.data_base.entity.AppUsageEntity
import com.lab42.skillsclub.data.data_base.entity.ConfigEntity
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import com.lab42.skillsclub.data.data_base.entity.StartNotificationEntity

@Database(
    version = 4,
    entities = [
        ProfileDbEntity::class,
        ConfigEntity::class,
        StartNotificationEntity::class,
        AppUsageEntity::class
    ]
)
abstract class AppDB : RoomDatabase() {

    abstract fun getProfileDao(): ProfileDao

    abstract fun getHelloDao(): HelloDao

    abstract fun getAppUsageDao(): AppUsageDao
}