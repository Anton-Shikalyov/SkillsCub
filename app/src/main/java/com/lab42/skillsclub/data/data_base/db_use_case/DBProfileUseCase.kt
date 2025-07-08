package com.lab42.skillsclub.data.data_base.db_use_case

import com.lab42.skillsclub.DB_Space.PROFILE
import com.lab42.skillsclub.data.data_base.DAO.ProfileDao
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import com.lab42.skillsclub.data.dto.response.Profile
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.ProfileMapInterface
import javax.inject.Inject

class DBProfileUseCase @Inject constructor(
    private val profileDao: ProfileDao,
    private val profileMapInterface: ProfileMapInterface,
)   {
    suspend fun saveAuthData(user: Profile) {
        profileDao.insertProfile(profileMapInterface.mapResponse(user))
    }

    suspend fun getProfile (): List<ProfileDbEntity> {
        return profileDao.selectProfile()
    }

    suspend fun deleteProfile () {
        return profileDao.deleteProfile()
    }

    suspend fun updateCurrentPosition(newPosition: Int) {
        profileDao.updateCurrentPosition(newPosition, PROFILE)
    }

    suspend fun updateCurrentPositionName(name: String) {
        profileDao.updateCurrentPositionName(name, PROFILE)
    }

}