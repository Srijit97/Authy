package com.srijit.authy_sdk.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.srijit.authy_sdk.utils.LoginResult
import com.srijit.authy_sdk.utils.putIfNotNull

internal class LoginViewModel : ViewModel() {
    private val _isLoginFlow = MutableLiveData(true)
    val isLoginFLow = _isLoginFlow as LiveData<Boolean>

    private val _authenticationSuccessful = MutableLiveData<LoginResult>(LoginResult.NoResult)
    val authenticationSuccessful = _authenticationSuccessful as LiveData<LoginResult>

    val emailId = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun setFlow(isLogin: Boolean) {
        _isLoginFlow.value = isLogin
    }

    fun authenticateWithEmailAndPassword() {
        isLoginFLow.value?.let {
            if (!it) {
                auth.createUserWithEmailAndPassword(this.emailId.value!!, this.password.value!!)
                    .addOnSuccessListener {
                        it.user?.let {
                            _authenticationSuccessful.value = LoginResult.LoginSuccessful(it)
                        }
                    }.addOnFailureListener { exception ->
                        exception.message?.let {
                            _authenticationSuccessful.value =
                                LoginResult.LoginError(errorMessage = it)
                        }
                    }
            } else {
                auth.signInWithEmailAndPassword(this.emailId.value!!, this.password.value!!)
                    .addOnSuccessListener {
                        it.user?.let {
                            _authenticationSuccessful.value = LoginResult.LoginSuccessful(it)
                        }
                    }.addOnFailureListener { exception ->
                        exception.message?.let {
                            _authenticationSuccessful.value =
                                LoginResult.LoginError(errorMessage = it)
                        }
                    }
            }

        }
    }
}