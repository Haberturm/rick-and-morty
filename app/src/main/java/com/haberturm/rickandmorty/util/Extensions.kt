package com.haberturm.rickandmorty.util

import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
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

fun TextView.setLinkText(
    string: String,
    actionOnClick: () -> Unit,
    linkStartPosition: Int,
    linkEndPosition: Int
){
    val spannableString = SpannableString(string)
    val clickableSpan = object : ClickableSpan(){
        override fun onClick(p0: View) {
            actionOnClick()
        }
    }
    spannableString.setSpan(clickableSpan,linkStartPosition, linkEndPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
}