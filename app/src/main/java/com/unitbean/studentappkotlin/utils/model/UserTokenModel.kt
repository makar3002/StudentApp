package com.unitbean.studentappkotlin.utils.model

import androidx.annotation.StringDef

class UserTokenModel(
    val userId: String,
    @TokenType
    val type: String) {

    @StringDef(
        TokenType.VK,
        TokenType.NotVK
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TokenType {
        companion object {
            const val VK = "VK"
            const val NotVK = "NotVK"
        }
    }
}