package com.srijit.authy_sdk

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.srijit.authy_sdk.ui.LoginActivity
import com.srijit.authy_sdk.utils.Authy
import com.srijit.authy_sdk.utils.Constants
import com.srijit.authy_sdk.utils.UserLoginStatus
import com.srijit.authy_sdk.utils.UserLoginStatusCallback

class AuthySdk {
    companion object {
        fun startAuthFlow(
            context: Context,
            authy: Authy
        ) {
            FirebaseApp.initializeApp(context)
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("authy", authy)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun getUserLoginStatus(
            context: Context,
            userLoginStatusCallback: UserLoginStatusCallback
        ) {
            var isDoctor = false
            FirebaseApp.initializeApp(context)
            val fireBaseAuth = FirebaseAuth.getInstance()
            val firebaseDatabase = Firebase.database.getReference(Constants.USERS_TABLE)

            if (fireBaseAuth.currentUser == null)
                userLoginStatusCallback.callback.invoke(UserLoginStatus.NotLoggedIn)
            else {
                val userId = fireBaseAuth.currentUser?.uid

                firebaseDatabase.child(Constants.DOCTORS_TABLE)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach {
                                if (it.value == userId)
                                    isDoctor = true
                            }
                            if (isDoctor)
                                userLoginStatusCallback.callback.invoke(UserLoginStatus.Doctor)
                            else
                                userLoginStatusCallback.callback.invoke(UserLoginStatus.Patient)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }
        }
    }

}