package com.unitbean.studentappkotlin.repository.profileHost

import com.unitbean.studentappkotlin.repository.UserService

class ProfileHostRepository(private val userService: UserService) : IProfileHostRepository {

    override fun isUserLogged(): Boolean = userService.isAuth()
}