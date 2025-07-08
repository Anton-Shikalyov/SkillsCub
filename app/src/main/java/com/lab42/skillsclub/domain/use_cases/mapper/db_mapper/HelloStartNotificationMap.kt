package com.lab42.skillsclub.domain.use_cases.mapper.db_mapper

import HelloResDTO
import com.lab42.skillsclub.DB_Space
import com.lab42.skillsclub.data.data_base.entity.StartNotificationEntity
import javax.inject.Inject

interface HelloStartNotificationInterface {
    fun mapResponse(res: HelloResDTO): StartNotificationEntity
}
class HelloStartNotificationMapImpl @Inject constructor(
) : HelloStartNotificationInterface {
    override fun mapResponse(res: HelloResDTO): StartNotificationEntity {
        return StartNotificationEntity(
            DB_Space.START_NOTIFICATION,
            res.data.startNotification.showNotification,
            res.data.startNotification.title,
            res.data.startNotification.text
        )
    }
}