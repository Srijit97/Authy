package com.srijit.authy_sdk.utils

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable

internal sealed class LoginResult {
    object NoResult : LoginResult()
    data class LoginSuccessful(
        val user: FirebaseUser,
        val userLoginStatus: UserLoginStatus
    ) : LoginResult()

    data class LoginError(val errorMessage: String) : LoginResult()
}

sealed class AuthResult {
    data class LoginSuccess(
        val user: FirebaseUser,
        val userLoginStatus: UserLoginStatus
    ) : AuthResult()
    object LoginError : AuthResult()
}

data class Authy(
    val data: Map<String, Any>,
    val authResult: (AuthResult) -> Unit
) : Serializable

sealed class UserLoginStatus {
    object Patient : UserLoginStatus()
    object Doctor : UserLoginStatus()
    object NotLoggedIn : UserLoginStatus()
}