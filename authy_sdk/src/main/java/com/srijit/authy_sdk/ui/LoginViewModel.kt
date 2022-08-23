package com.srijit.authy_sdk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.srijit.authy_sdk.utils.Constants
import com.srijit.authy_sdk.utils.LoginResult

internal class LoginViewModel : ViewModel() {
    private val _isLoginFlow = MutableLiveData(true)
    val isLoginFLow = _isLoginFlow as LiveData<Boolean>

    var isDoctor = false

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
                        it.user?.let { user ->
                            addUserToDatabase(user)
                            _authenticationSuccessful.value = LoginResult.LoginSuccessful(user)
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
                        it.user?.let { user ->
                            _authenticationSuccessful.value = LoginResult.LoginSuccessful(user)
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

    private fun addUserToDatabase(user: FirebaseUser) {
        val database = Firebase.database.getReference(Constants.USERS_TABLE)

        if (isDoctor)
            database.child(Constants.DOCTORS_TABLE).push().setValue(user.uid)
        else
            database.child(Constants.PATIENTS_TABLE).push().setValue(user.uid)
    }

    fun setUserType(isDoctorUserType: Boolean) {
        isDoctor = isDoctorUserType
    }
}