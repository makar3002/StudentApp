package com.unitbean.studentappkotlin.ui.profileHost.interactors

import com.unitbean.studentappkotlin.utils.repository.profileHost.IProfileHostRepository

class ProfileHostInteractor(private val repository: IProfileHostRepository) {

    fun isUserLogged(): Boolean {
        return repository.isUserLogged()
    }
}