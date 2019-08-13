package com.unitbean.studentappkotlin.utils.repository.main

import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel
import com.vk.api.sdk.auth.VKAccessToken

interface IMainRepository {
    suspend fun loginWithVk(res: VKAccessToken)
    suspend fun loginWithoutVk(userId: String)
    fun saveUser(user: UserModel)
    fun isAuthSuccess(): Boolean
    fun logout()
    suspend fun loadVkUser(token: UserTokenModel): UserModel
    suspend fun loadNotVkUser(token: UserTokenModel): UserModel
    suspend fun restoreUserToken(): UserTokenModel?
}