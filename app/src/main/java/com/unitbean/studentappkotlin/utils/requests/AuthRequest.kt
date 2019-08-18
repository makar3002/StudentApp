package com.unitbean.studentappkotlin.utils.requests

import com.unitbean.studentappkotlin.utils.model.UserTokenModel

class AuthRequest (
    val studentId: String,
    @UserTokenModel.TokenType
    val type: String
)