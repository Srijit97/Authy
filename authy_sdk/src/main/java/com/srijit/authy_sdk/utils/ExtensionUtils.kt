package com.srijit.authy_sdk.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter




fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <E, R> MutableMap<E, R>.putIfNotNull(key: E, value: R?) {
    value?.let { nonNullValue -> this.apply { put(key, nonNullValue) } }
}

@BindingAdapter("imageResource")
fun ImageView.setImageResource(resource: Int) {
    setImageResource(resource)
}