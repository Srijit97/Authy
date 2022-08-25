package com.srijit.authy_sdk.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


internal sealed class LoginResult  {
    object NoResult : LoginResult()

    data class LoginSuccessful(
        val userLoginStatus: UserLoginStatus
    ) : LoginResult()

    data class LoginError(val errorMessage: String) : LoginResult()
}

data class Authy(
    @Transient
    val authResult: (AuthResult) -> Unit
) : Serializable

sealed class AuthResult : Serializable {
    object PatientLoggedIn : AuthResult()

    object DoctorLoggedIn : AuthResult()

    object LoginError : AuthResult()
}

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