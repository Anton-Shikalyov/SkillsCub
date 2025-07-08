package com.lab42.skillsclub.domain.use_cases.mapper.db_mapper

import com.lab42.skillsclub.DB_Space
import com.lab42.skillsclub.data.dto.response.Profile
import com.lab42.skillsclub.data.data_base.entity.ProfileDbEntity
import javax.inject.Inject

interface ProfileMapInterface {
    fun mapResponse(res: Profile): ProfileDbEntity
}
class ProfileMapImpl @Inject constructor(
) : ProfileMapInterface {
    override fun mapResponse(res: Profile): ProfileDbEntity {
        return ProfileDbEntity(
            DB_Space.PROFILE,
            res.login,
            res.name,
            res.gender,
            res.currentPosition,
            res.lastSection,
            res.lastProgram,
            res.lastStep,
            ""
        )
    }
}