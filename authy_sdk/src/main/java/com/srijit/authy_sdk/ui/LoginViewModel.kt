package com.srijit.authy_sdk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.srijit.authy_sdk.utils.Constants
import com.srijit.authy_sdk.utils.LoginResult
import com.srijit.authy_sdk.utils.UserLoginStatus

internal class LoginViewModel : ViewModel() {
    private val _isLoginFlow = MutableLiveData(true)
    val isLoginFLow = _isLoginFlow as LiveData<Boolean>

    private val _loaderVisibility = MutableLiveData(false)
    val loaderVisibility = _loaderVisibility as LiveData<Boolean>

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
        _loaderVisibility.value = true
        isLoginFLow.value?.let {
            if (!it) {
                auth.createUserWithEmailAndPassword(this.emailId.value!!, this.password.value!!)
                    .addOnSuccessListener {
                        it.user?.let { user ->
                            val userLoginStatus =
                                if (isDoctor)
                                    UserLoginStatus.Doctor
                                else
                                    UserLoginStatus.Patient
                            addUserToDatabase(user, userLoginStatus)
                        }
                    }.addOnFailureListener { exception ->
                        exception.message?.let {
                            _authenticationSuccessful.value =
                                LoginResult.LoginError(errorMessage = it)
                            _loaderVisibility.value = false
                        }
                    }
            } else {
                auth.signInWithEmailAndPassword(this.emailId.value!!, this.password.value!!)
                    .addOnSuccessListener {
                        it.user?.let { user ->
                            getUserType(user)
                        }
                    }.addOnFailureListener { exception ->
                        exception.message?.let {
                            _authenticationSuccessful.value =
                                LoginResult.LoginError(errorMessage = it)
                            _loaderVisibility.value = false
                        }
                    }
            }

        }
    }

    private fun getUserType(user: FirebaseUser) {
        val database = Firebase.database.getReference(Constants.USERS_TABLE)
        database.child(Constants.DOCTORS_TABLE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.value == user.uid)
                            isDoctor = true
                    }
                    if (isDoctor)
                        _authenticationSuccessful.value =
                            LoginResult.LoginSuccessful(UserLoginStatus.Doctor)
                    else
                        _authenticationSuccessful.value =
                            LoginResult.LoginSuccessful(UserLoginStatus.Patient)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


    }

    private fun addUserToDatabase(user: FirebaseUser, userLoginStatus: UserLoginStatus) {
        val database = Firebase.database.getReference(Constants.USERS_TABLE)

        if (isDoctor)
            database.child(Constants.DOCTORS_TABLE).push().setValue(user.uid)
        else
            database.child(Constants.PATIENTS_TABLE).push().setValue(user.uid)

        _authenticationSuccessful.value =
            LoginResult.LoginSuccessful(userLoginStatus)
        _loaderVisibility.value = false
    }

    fun setUserType(isDoctorUserType: Boolean) {
        isDoctor = isDoctorUserType
    }
}