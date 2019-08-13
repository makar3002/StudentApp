package com.unitbean.studentappkotlin.utils.repository.profile

import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel

class ProfileRepository(private val apiService: ApiService, private val userService: UserService) : IProfileRepository {

    override suspend fun getUser(): UserModel? {
        userService.forceUpdateUser(apiService, userService.userToken?.userId ?: throw IllegalStateException("Token not loaded"), userService.userToken?.type ?: throw IllegalStateException("Token not loaded"))
        return userService.user ?: UserModel("", "firstName", "lastName","",  "","", "")
    }

    override suspend fun updateUser(token: UserTokenModel): UserModel? {
        return userService.forceUpdateUser(apiService, token.userId, token.type)
    }

    override fun isUserLoaded(): Boolean = userService.userToken != null && userService.user != null

    override fun logout() {
        userService.cleanData(apiService)
    }

    override fun getToken(): UserTokenModel? = userService.userToken
}