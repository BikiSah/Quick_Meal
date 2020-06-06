

package com.biki.quickmeal.extensions

import android.text.TextUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.regex.Pattern


fun Disposable.addToCompositeDisposable(composite: CompositeDisposable) {
    composite.add(this)
}

fun String?.notEmpty(): Boolean =
    !TextUtils.isEmpty(this?.trim())
