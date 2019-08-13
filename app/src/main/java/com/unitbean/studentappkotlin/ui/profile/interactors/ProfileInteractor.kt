package com.unitbean.studentappkotlin.ui.profile.interactors

import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import com.unitbean.studentappkotlin.utils.repository.profile.IProfileRepository

class ProfileInteractor(private val repository: IProfileRepository) {
    fun logout() {
        repository.logout()
    }

    suspend fun loadProfile(): UserModel {
        return if (repository.isUserLoaded()) {
            val user: UserModel = repository.getUser() ?: throw IllegalStateException("User not loaded")
            UserModel(user.id, user.firstName, user.lastName, user.institute, user.course, user.group, user.recordBook)
        } else {
            val user = repository.updateUser(repository.getToken() ?: throw IllegalStateException("Token not loaded"))
            user ?: throw IllegalStateException("User not loaded")
        }
    }



}