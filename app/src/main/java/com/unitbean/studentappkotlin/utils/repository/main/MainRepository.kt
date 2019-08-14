package com.unitbean.studentappkotlin.utils.repository.main

import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.vk.api.sdk.auth.VKAccessToken

class MainRepository(private val apiService: ApiService,
                     private val userService: UserService) : IMainRepository {

    override suspend fun loginWithVk(res: VKAccessToken) {
        userService.loginWithVk(apiService, res)
    }

    override suspend fun loginWithoutVk(userId: String) {
        userService.loginWithoutVk(userId)
    }

    override fun saveUser(user: UserModel) {
        userService.saveUser(user)
    }

    override fun logout() = userService.logout(apiService)

    override fun isAuthSuccess(): Boolean = userService.isAuth()

    override suspend fun loadVkUser(token: UserTokenModel): UserModel {
        val user = userService.forceUpdateUser(apiService, token.userId, token.type)
        return user
    }
    override suspend fun loadNotVkUser(token: UserTokenModel): UserModel {
        val user = userService.forceUpdateUser(apiService, token.userId, token.type)
        return user
    }

    override suspend fun restoreUserToken(): UserTokenModel? = userService.restoreUserToken()
}