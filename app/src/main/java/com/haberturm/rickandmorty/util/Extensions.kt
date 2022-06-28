package com.haberturm.rickandmorty.util

import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide


fun ImageView.loadImage(url: String){
    val uri = url.toUri().buildUpon().scheme("https").build()
    Glide.with(this.context)
        .load(uri)
        .into(this)

}


fun TextView.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: TextView.() -> Unit) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}