package com.bilibililevel6.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import com.bilibililevel6.R

/**
 * author：liuzipeng
 * time: 2022/11/26 01:11
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet, defStyle) {
    private val animDrawable by lazy { background as AnimationDrawable }

    init {
        setBackgroundResource(R.drawable.loading_anim)
    }

    fun starLoading() {
        visibility = View.VISIBLE
        animDrawable.start()
    }

    fun endLoading() {
        visibility = View.GONE
        animDrawable.stop()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        endLoading()
    }
}