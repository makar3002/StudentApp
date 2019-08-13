package com.unitbean.studentappkotlin.utils.repository.auth

import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel

class AuthRepository(private val apiService: ApiService,
                     private val userService: UserService) : IAuthRepository {
    override fun getUser(): UserModel? = userService.user

    override suspend fun updateUser(token: UserTokenModel): UserModel? {
        return userService.forceUpdateUser(apiService, token.userId, token.type)
    }

    override fun isUserLoaded(): Boolean = userService.userToken != null && userService.user != null

    override suspend fun authUser(user: UserModel){
        if (userService.userToken?.type == UserTokenModel.TokenType.VK) {
            userService.authUser(
                apiService,
                UserModel(
                    userService.userToken?.userId!!,
                    user.firstName,
                    user.lastName,
                    user.institute,
                    user.course,
                    user.group,
                    user.recordBook
                )
            )
        } else {
            val userId: String =
                user.firstName + user.lastName + user.institute + user.course + user.group + user.recordBook
            userService.authUser(
                apiService,
                UserModel(
                    userId,
                    user.firstName,
                    user.lastName,
                    user.institute,
                    user.course,
                    user.group,
                    user.recordBook
                )
            )
        }

    }

    override fun getToken(): UserTokenModel? = userService.restoreUserToken()

    override fun logout() {
        userService.cleanData(apiService)
    }


}