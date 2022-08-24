package com.srijit.authy_sdk.utils

import android.os.Parcelable
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

internal sealed class LoginResult {
    object NoResult : LoginResult()
    data class LoginSuccessful(
        val user: FirebaseUser,
        val userLoginStatus: UserLoginStatus
    ) : LoginResult()

    data class LoginError(val errorMessage: String) : LoginResult()
}


sealed class AuthResult : Parcelable {
    @Parcelize
    data class LoginSuccess(
        val userLoginStatus: UserLoginStatus
    ) : AuthResult()

    @Parcelize
    object LoginError : AuthResult()
}

@Parcelize
data class Authy(
    val authResult: (AuthResult) -> Unit
) : Parcelable

@Parcelize
data class Random(
    val authResult: (String) -> Unit
) : Parcelable

@Parcelize
data class UserLoginStatusCallback(
    val callback: (UserLoginStatus) -> Unit
) : Parcelable


sealed class UserLoginStatus : Parcelable {
    @Parcelize
    object Patient : UserLoginStatus()

    @Parcelize
    object Doctor : UserLoginStatus()

    @Parcelize
    object NotLoggedIn : UserLoginStatus()
}