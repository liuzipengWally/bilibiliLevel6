package com.bilibililevel6.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 刘自鹏 on 2017/5/26.
 * Email:liuzipeng@meituan.com
 */

public class ImageLoader {

    public static void loadCircleImg(Context context, int resId, ImageView imageView, int errorResId, int defaultResId) {
        Glide.with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultResId)
                .error(errorResId)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void loadCircleImg(final Context context, final String url, ImageView imageView, int defaultResId) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultResId)
                .error(defaultResId)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void loadCircleImg(Context context, File file, ImageView imageView, int defaultResId) {
        Glide.with(context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultResId)
                .error(defaultResId)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }


    public static void loadImg(Context context, String url, ImageView imageView, int defaultResId) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultResId)
                .error(defaultResId)
                .into(imageView);
    }


    public static void loadRoundedCornersImg(Context context,
                                             int resId,
                                             ImageView imageView,
                                             int defaultResId,
                                             int radius,
                                             int margin) {
        Glide.with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(defaultResId)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(imageView);
    }

    public static void loadRoundedCornersImg(Context context,
                                             String url,
                                             ImageView imageView,
                                             int defaultResId,
                                             int radius,
                                             int margin) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(defaultResId)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(imageView);
    }

    public static void loadRoundedCornersImg(Context context,
                                             File file,
                                             ImageView imageView,
                                             int defaultResId,
                                             int radius,
                                             int margin) {
        Glide.with(context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(defaultResId)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(imageView);
    }

    public static void loadRoundedCornersImg(Context context,
                                             GlideUrl glideUrl,
                                             ImageView imageView,
                                             int defaultResId,
                                             int radius,
                                             int margin) {
        Glide.with(context)
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(defaultResId)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(imageView);
    }
}
