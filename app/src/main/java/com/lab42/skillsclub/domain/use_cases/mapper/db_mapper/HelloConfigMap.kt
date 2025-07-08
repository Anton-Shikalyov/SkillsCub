package com.lab42.skillsclub.domain.use_cases.mapper.db_mapper

import HelloResDTO
import com.lab42.skillsclub.DB_Space
import com.lab42.skillsclub.data.data_base.entity.ConfigEntity
import javax.inject.Inject

interface HelloConfigMapInterface {
    fun mapResponse(res: HelloResDTO): ConfigEntity
}
class HelloConfigMapImpl @Inject constructor(
) : HelloConfigMapInterface {
    override fun mapResponse(res: HelloResDTO): ConfigEntity {
        return ConfigEntity(
            DB_Space.CONFIG,
            res.data.config.testSuccessThreshold,
            res.data.config.androidLastVer,
            res.data.config.androidMinVer,
            res.data.config.androidUpdateLink,
            res.data.config.iosLastVer,
            res.data.config.iosMinVer,
            res.data.config.iosUpdateLink,
            res.data.config.appAvailable,
            res.data.config.webviewCss,
            res.data.config.webviewDarkCss
        )
    }
}