package com.unitbean.studentappkotlin.repository.profile

import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel

interface IProfileRepository {
    fun logout()
    suspend fun getUser(): UserModel?
    fun isUserLoaded(): Boolean
    fun getToken(): UserTokenModel?
    suspend fun updateUser(token: UserTokenModel): UserModel?
}