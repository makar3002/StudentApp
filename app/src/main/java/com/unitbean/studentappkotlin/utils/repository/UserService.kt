package com.unitbean.studentappkotlin.utils.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.repository.requests.AuthRequest
import com.unitbean.studentappkotlin.utils.repository.responses.AuthResponse
import com.vk.api.sdk.auth.VKAccessToken
import java.text.SimpleDateFormat
import java.util.*

/**
 * Не внедрять сюда зависимость от [ApiService], иначе будет Dependency Cycle
 * Лучше использовать в методах явную передачу объекта [ApiService]
 *
 * Не хочется выносить в static-field конфликтые значения
 */
class UserService(private val prefs: SharedPreferences,
                  private val editor: SharedPreferences.Editor) {
    var userToken: UserTokenModel? = null
    var user: UserModel? = null

    suspend fun forceUpdateUser(firebaseApi: ApiService,
                                userId: String,
                                @UserTokenModel.TokenType type: String): UserModel {
        val updatedUser = firebaseApi.getUser(AuthRequest(userId, type))
        return updatedUser.toUserViewModel().apply {
            this@UserService.user = this
        }
    }
    suspend fun authUser(firebaseApi: ApiService,
                                user: UserModel) {
        this@UserService.user = user
        firebaseApi.changeUser(user.id, user.firstName, user.lastName, user.institute, user.course, user.group, user.recordBook)
    }

    fun restoreUserToken(): UserTokenModel? {
        val token = Gson().fromJson(prefs.getString(SAVED_USER, ""), UserTokenModel::class.java)
        this.userToken = token
        return token
    }

    suspend fun loginWithVk(firebaseApi: ApiService, res: VKAccessToken) {
        val updatedUser = firebaseApi.getUser(AuthRequest(res.userId.toString(), UserTokenModel.TokenType.VK))
        userToken = UserTokenModel(res.userId.toString(), UserTokenModel.TokenType.VK)
        editor.putString(SAVED_USER, Gson().toJson(userToken)).apply()
    }

    suspend fun loginWithoutVk(firebaseApi: ApiService, userId: String) {
        val updatedUser = firebaseApi.getUser(AuthRequest(userId, UserTokenModel.TokenType.NotVK))
        userToken = UserTokenModel(userId, UserTokenModel.TokenType.NotVK)
        editor.putString(SAVED_USER, Gson().toJson(userToken)).apply()
    }

    fun saveUser(userModel: UserModel) {
        user = userModel
    }

    fun isAuth(): Boolean = userToken != null

    fun logout(apiService: ApiService) {
        user = null
        restoreUserToken()
        if (userToken?.type == UserTokenModel.TokenType.NotVK) apiService.deleteUser(userToken?.userId ?: "")
        userToken = null
        editor.clear().apply()

    }

    fun cleanData(apiService: ApiService) {
        logout(apiService)
    }

    private fun AuthResponse.toUserViewModel(): UserModel {
        return UserModel(id,
            firstName,
            lastName,
            institute,
            course,
            group,
            recordBook
            )
    }

    companion object {
        private const val SAVED_USER = "saved_user_v2"
        private const val SAVED_EVENTS = "saved_events"
    }
}