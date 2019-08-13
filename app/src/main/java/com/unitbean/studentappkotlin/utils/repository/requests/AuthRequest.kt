package com.unitbean.studentappkotlin.utils.repository.requests

import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel

class AuthRequest (
    val studentId: String,
    @UserTokenModel.TokenType
    val type: String
)