package com.unitbean.studentappkotlin.repository.auth

import com.unitbean.studentappkotlin.repository.ApiService
import com.unitbean.studentappkotlin.repository.UserService
import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel

class AuthRepository(private val apiService: ApiService,
                     private val userService: UserService
) : IAuthRepository {
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
                    user.recordBook,
                    user.semester
                )
            )
        } else {
            val userId: String =
                user.firstName + user.lastName + user.institute + user.course + user.group + user.recordBook
            userService.loginWithoutVk(userId)
            userService.authUser(
                apiService,
                UserModel(
                    userId,
                    user.firstName,
                    user.lastName,
                    user.institute,
                    user.course,
                    user.group,
                    user.recordBook,
                    user.semester
                )
            )
        }

    }

    override fun getToken(): UserTokenModel? = userService.restoreUserToken()

    override fun logout() {
        userService.cleanData(apiService)
    }


}