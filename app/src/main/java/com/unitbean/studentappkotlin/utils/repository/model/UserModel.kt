package com.unitbean.studentappkotlin.utils.repository.model

data class UserModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val institute: String,
    val course: String,
    val group: String,
    val recordBook: String
)