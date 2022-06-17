package com.haberturm.rickandmorty.util

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide


fun ImageView.loadImage(url: String){
    val uri = url.toUri().buildUpon().scheme("https").build()
    Glide.with(this.context)
        .load(uri)
        .into(this)

}