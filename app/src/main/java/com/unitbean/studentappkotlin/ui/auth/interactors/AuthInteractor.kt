package com.unitbean.studentappkotlin.ui.auth.interactors

import com.unitbean.studentappkotlin.repository.auth.IAuthRepository
import com.unitbean.studentappkotlin.utils.model.UserModel

class AuthInteractor (private val repository: IAuthRepository) {
    fun logout() {
        repository.logout()
    }

    suspend fun loadProfile(): UserModel {
        return if (repository.isUserLoaded()) {
            repository.getUser() ?: throw IllegalStateException("User not loaded")
        } else {
            val user = repository.updateUser(repository.getToken() ?: throw IllegalStateException("Token not loaded"))
            user ?: throw IllegalStateException("User not loaded")
        }
    }

    suspend fun authUser(user: UserModel){
        repository.authUser(user)
    }
}