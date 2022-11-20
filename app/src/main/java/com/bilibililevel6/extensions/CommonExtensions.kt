package com.bilibililevel6.extensions

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * author：liuzipeng
 * time: 2022/11/20 22:41
 */

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}


fun Context.showToast(msgResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msgResId, duration).show()
}

fun Fragment.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    requireActivity().showToast(msg, duration)
}


fun Fragment.showToast(msgResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    requireActivity().showToast(msgResId, duration)
}