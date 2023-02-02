package com.bilibililevel6.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import com.bumptech.glide.load.model.GlideUrl
import java.io.File

/**
 * Created by 刘自鹏 on 2017/5/26.
 * Email:liuzipeng@meituan.com
 */
object ImageLoader {
    fun loadCircleImg(
        context: Context,
        resId: Int,
        imageView: ImageView,
        errorResId: Int,
        defaultResId: Int
    ) {
        Glide.with(context)
            .load(resId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(defaultResId)
            .error(errorResId)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }

    fun loadCircleImg(context: Context, url: String, imageView: ImageView, defaultResId: Int) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(defaultResId)
            .error(defaultResId)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }

    fun loadCircleImg(context: Context, file: File, imageView: ImageView, defaultResId: Int) {
        Glide.with(context)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(defaultResId)
            .error(defaultResId)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }

    fun loadImg(context: Context, url: String, imageView: ImageView, defaultResId: Int = 0) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(defaultResId)
            .error(defaultResId)
            .into(imageView)
    }

    fun loadRoundedCornersImg(
        context: Context,
        resId: Int,
        imageView: ImageView,
        defaultResId: Int,
        radius: Int,
        margin: Int
    ) {
        Glide.with(context)
            .load(resId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(defaultResId)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin)))
            .into(imageView)
    }

    fun loadRoundedCornersImg(
        context: Context,
        url: String,
        imageView: ImageView,
        defaultResId: Int,
        radius: Int,
        margin: Int
    ) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(defaultResId)
            .error(defaultResId)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin)))
            .into(imageView)
    }

    fun loadRoundedCornersImg(
        context: Context,
        file: File,
        imageView: ImageView,
        defaultResId: Int,
        radius: Int,
        margin: Int
    ) {
        Glide.with(context)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(defaultResId)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin)))
            .into(imageView)
    }

    fun loadRoundedCornersImg(
        context: Context,
        glideUrl: GlideUrl,
        imageView: ImageView,
        defaultResId: Int,
        radius: Int,
        margin: Int
    ) {
        Glide.with(context)
            .load(glideUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(defaultResId)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin)))
            .into(imageView)
    }
}