

package com.biki.quickmeal.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    abstract var disposable : CompositeDisposable

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}