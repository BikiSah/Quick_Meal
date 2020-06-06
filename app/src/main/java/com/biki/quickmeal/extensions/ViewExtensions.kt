

package com.biki.quickmeal.extensions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun EditText.isEmpty(): Boolean =
        TextUtils.isEmpty(this.text.toString().trim())

fun EditText.toTextString(): String =
        this.text.toString().trim()


fun View.hideSoftKey() {
    val view = this
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


