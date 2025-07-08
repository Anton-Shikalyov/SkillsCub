package com.lab42.skillsclub.data.data_base.db_use_case

import HelloResDTO
import com.lab42.skillsclub.data.data_base.DAO.HelloDao
import com.lab42.skillsclub.data.data_base.entity.ConfigEntity
import com.lab42.skillsclub.data.data_base.entity.StartNotificationEntity
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloConfigMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.db_mapper.HelloStartNotificationInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DBHelloUseCase @Inject constructor(
    private val helloDao: HelloDao,
    private val helloConfigMapInterface: HelloConfigMapInterface,
    private val helloStartNotificationInterface: HelloStartNotificationInterface
)   {

    suspend fun insertHelloConfig(helloRes: HelloResDTO) {
        helloDao.insertConfig(helloConfigMapInterface.mapResponse(helloRes))
    }

    suspend fun insertStartNotification(helloRes: HelloResDTO) {
        helloDao.insertStartNotification(helloStartNotificationInterface.mapResponse(helloRes))
    }

     fun getHelloConfig (): Flow<List<ConfigEntity?>> {
        return helloDao.selectConfig()
    }

    suspend fun getStartNotification (): List<StartNotificationEntity> {
        return helloDao.selectStartNotification()
    }

    suspend fun deleteHelloConfig () {
        return helloDao.deleteConfig()
    }

    suspend fun deleteStartNotification () {
        return helloDao.deleteStartNotification()
    }


}