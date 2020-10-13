package com.focusonme.tflitesample.extension.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

public class GlideHelper {

    public static void loadImage(Context context,
                                 String filePath,
                                 AppCompatImageView imageView) {
        Glide.with(context).load(filePath).into(imageView);
    }

    public static void loadImage(Context context,
                                 int resId,
                                 AppCompatImageView imageView) {
        Glide.with(context).load(resId).into(imageView);
    }
}
