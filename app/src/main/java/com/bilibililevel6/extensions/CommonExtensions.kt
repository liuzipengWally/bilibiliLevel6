package com.bilibililevel6.extensions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.leanback.widget.VerticalGridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bilibililevel6.PreferencesSerializer
import com.bilibililevel6.home.popular.intent.PopularListIntent

/**
 * author：liuzipeng
 * time: 2022/11/20 22:41
 */

val Context.dataStore by dataStore("bilibiliLevel6.json", serializer = PreferencesSerializer)

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

inline fun VerticalGridView.onLoadMore(spanCount: Int, crossinline function: () -> Unit) {
    setOnChildViewHolderSelectedListener(object : OnChildViewHolderSelectedListener() {
        override fun onChildViewHolderSelected(
            parent: RecyclerView?, child: RecyclerView.ViewHolder?, position: Int, subposition: Int
        ) {
            val itemCount = parent?.adapter?.itemCount ?: 0
            if (itemCount == 0) return
            if (position >= itemCount - spanCount * 2) {
                function()
            }
        }
    })
}