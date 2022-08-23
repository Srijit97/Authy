package com.srijit.authy_sdk

import android.content.Context
import android.content.Intent
import com.srijit.authy_sdk.ui.LoginActivity
import com.srijit.authy_sdk.utils.Authy

class AuthyBuilder {


    fun startAuthFlow(context: Context, authy: Authy) {
        val intent = Intent(context,LoginActivity::class.java)
        intent.putExtra("authy",authy)
        context.startActivity(intent)
    }
}