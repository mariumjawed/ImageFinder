package com.android.searchimage.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android.searchimage.R
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url).placeholder(R.drawable.tab_unselected).into(imageView)
}
