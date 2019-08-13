package com.unitbean.studentappkotlin.ui.main.interactors

import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.repository.main.IMainRepository
import com.vk.api.sdk.auth.VKAccessToken

class MainInteractor(val repository: IMainRepository) {
    suspend fun loadSavedUser() {
        val token = repository.restoreUserToken()
        if (token != null) {
            val user = when (token.type) {
                UserTokenModel.TokenType.VK -> repository.loadVkUser(token)
                UserTokenModel.TokenType.NotVK -> repository.loadNotVkUser(token)
                else -> repository.loadNotVkUser(token)
            }

            repository.saveUser(user)
        }
    }
    fun isAuth(): Boolean = repository.isAuthSuccess()

    suspend fun onVkSignIn(res: VKAccessToken): Boolean {
        repository.loginWithVk(res)
        return repository.isAuthSuccess()
    }

    suspend fun onNotVkSignIn(userId: String): Boolean {
        repository.loginWithoutVk(userId)
        return repository.isAuthSuccess()
    }
}