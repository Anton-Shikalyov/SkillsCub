package com.lab42.skillsclub.data.data_base.db_use_case

import com.lab42.skillsclub.DB_Space
import com.lab42.skillsclub.data.data_base.DAO.AppUsageDao
import com.lab42.skillsclub.data.data_base.entity.AppUsageEntity
import com.lab42.skillsclub.data.dto.request.AppUsageReqDTO
import javax.inject.Inject

class DBDateUseCase @Inject constructor(
    private val appUsagesDao: AppUsageDao,
) {
    suspend fun insertAppUsagesDao(date: AppUsageReqDTO) {
        appUsagesDao.insertDate(AppUsageEntity(DB_Space.DATE, date.date, date.mins))
    }
    suspend fun updateMins (mins: Int) {
        appUsagesDao.updateMinsByDate(mins)
    }
    suspend fun getAppUsages (): List<AppUsageEntity> {
        return appUsagesDao.selectDate()
    }

    suspend fun deleteAppUsages () {
        return appUsagesDao.deleteDate()
    }

}