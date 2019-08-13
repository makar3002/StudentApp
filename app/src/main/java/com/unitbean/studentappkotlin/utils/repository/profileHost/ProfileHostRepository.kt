package com.unitbean.studentappkotlin.utils.repository.profileHost

import com.unitbean.studentappkotlin.utils.repository.UserService

class ProfileHostRepository(private val userService: UserService) : IProfileHostRepository {

    override fun isUserLogged(): Boolean = userService.isAuth()
}