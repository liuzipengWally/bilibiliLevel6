package com.bilibililevel6.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bilibililevel6.R
import com.bilibililevel6.databinding.LayoutEmptyViewBinding

/**
 * author：liuzipeng
 * time: 2022/11/26 02:21
 */
class EmptyView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {
    private val viewBinding: LayoutEmptyViewBinding =
        LayoutEmptyViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusable = FOCUSABLE
        }
        isClickable = true
    }

    fun showEmptyView(type: EmptyViewType) {
        visibility = View.VISIBLE
        when (type) {
            EmptyViewType.LOAD_ERROR -> {
                viewBinding.tipsImg.setImageResource(R.drawable.img_loading_error)
                viewBinding.tipsText.text = "糟糕！被未知力量干扰了，按确认键重试~"
            }
            EmptyViewType.NO_DATA -> {
                viewBinding.tipsImg.setImageResource(R.drawable.img_empty_page)
                viewBinding.tipsText.text = "阿偶! 这里什么也没有诶~"
            }
        }
    }

    fun hideEmptyView() {
        visibility = View.GONE
    }
}

enum class EmptyViewType {
    NO_DATA, LOAD_ERROR
}