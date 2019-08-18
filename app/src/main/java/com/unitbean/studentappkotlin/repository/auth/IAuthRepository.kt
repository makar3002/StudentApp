package com.unitbean.studentappkotlin.repository.auth

import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel

interface IAuthRepository {
    fun logout()
    fun getUser(): UserModel?
    fun isUserLoaded(): Boolean
    fun getToken(): UserTokenModel?
    suspend fun updateUser(token: UserTokenModel): UserModel?
    suspend fun authUser(user: UserModel)
}