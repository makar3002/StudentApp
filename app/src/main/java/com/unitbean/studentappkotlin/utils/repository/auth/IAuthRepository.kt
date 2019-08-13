package com.unitbean.studentappkotlin.utils.repository.auth

import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel

interface IAuthRepository {
    fun logout()
    fun getUser(): UserModel?
    fun isUserLoaded(): Boolean
    fun getToken(): UserTokenModel?
    suspend fun updateUser(token: UserTokenModel): UserModel?
    suspend fun authUser(user: UserModel)
}