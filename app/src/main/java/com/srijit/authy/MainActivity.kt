package com.srijit.authy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.srijit.authy_sdk.AuthySdk
import com.srijit.authy_sdk.utils.AuthResult
import com.srijit.authy_sdk.utils.Authy
import com.srijit.authy_sdk.utils.UserLoginStatus
import com.srijit.authy_sdk.utils.UserLoginStatusCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_start).setOnClickListener {
            AuthySdk.startAuthFlow(this, Authy {
                when (it) {
                    is AuthResult.LoginError -> Log.d("msg1", "error")
                    is AuthResult.LoginSuccess -> Log.d("msg1", "success$it")
                }
            })

            AuthySdk.getUserLoginStatus(this, UserLoginStatusCallback {
                when (it) {
                    UserLoginStatus.Doctor -> Log.d("msg1", "doctor")
                    UserLoginStatus.NotLoggedIn -> Log.d("msg1", "not logged in")
                    UserLoginStatus.Patient -> Log.d("msg1", "patient")
                }
            })

        }
    }
}