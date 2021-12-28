package com.mvvm.ecommerce.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mvvm.ecommerce.R

class Utils {
    companion object{
        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.product_placeholder)
                    .into(imageView)
            }
        }
    }
}