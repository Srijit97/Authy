package com.srijit.authy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.srijit.authy_sdk.AuthyBuilder
import com.srijit.authy_sdk.utils.AuthResult
import com.srijit.authy_sdk.utils.Authy

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_start).setOnClickListener {
            AuthyBuilder().startAuthFlow(this, Authy(emptyMap()) {
                when(it){
                    is AuthResult.LoginError -> Log.d("msg1","error")
                    is AuthResult.LoginSuccess -> Log.d("msg1", "success$it")
                }
            })
        }
    }
}