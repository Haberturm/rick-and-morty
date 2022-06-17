package com.haberturm.rickandmorty.presentation.adapters

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}


fun ImageView.loadImage(url: String){
    val uri = url.toUri().buildUpon().scheme("https").build()
    Glide.with(this.context)
        .load(uri)
        .into(this)

}