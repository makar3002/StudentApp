package com.unitbean.studentappkotlin.utils.repository.profile

import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel

interface IProfileRepository {
    fun logout()
    suspend fun getUser(): UserModel?
    fun isUserLoaded(): Boolean
    fun getToken(): UserTokenModel?
    suspend fun updateUser(token: UserTokenModel): UserModel?
}