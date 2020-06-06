

package com.biki.quickmeal.eventbus

import android.util.SparseArray
import androidx.annotation.IntDef
import androidx.annotation.NonNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject


/**
 * Class to send various events across the application in Reactive way.
 *
 */
object RxBus {

    private val sSubjectMap = SparseArray<PublishSubject<Any>>()
    private val sSubscriptionsMap = HashMap<Any, CompositeDisposable>()

    const val SUBJECT_CONFIGURED_TAG = 0


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SUBJECT_CONFIGURED_TAG)
    internal annotation class Subject

    /**
     * Get the subject or create it if it's not already in memory.
     */
    @NonNull
    private fun getSubject(@Subject subjectCode: Int): PublishSubject<Any> {
        var subject = sSubjectMap.get(subjectCode)
        if (subject == null) {
            subject = PublishSubject.create<Any>()
            subject!!.subscribeOn(AndroidSchedulers.mainThread())
            sSubjectMap.put(subjectCode, subject)
        }

        return subject
    }

    /**
     * Get the CompositeSubscription or create it if it's not already in memory.
     */
    private fun getCompositeDisposable(`object`: Any): CompositeDisposable {
        var compositeSubscription = sSubscriptionsMap[`object`]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
            sSubscriptionsMap[`object`] = compositeSubscription
        }

        return compositeSubscription
    }

    /**
     * Subscribe to the specified subject and listen for updates on that subject. Pass in an object to associate
     * your registration with, so that you can unsubscribe later.
     * <br></br><br></br>
     * **Note:** Make sure to call [RxBus.unregister] to avoid memory leaks.
     */
    fun subscribe(@Subject subject: Int, lifecycle: Any, action: Consumer<Any>) {
        val subscription = getSubject(subject).subscribe( action )
        getCompositeDisposable(lifecycle).add(subscription)
    }

    /**
     * Unregisters this object from the bus, removing all subscriptions.
     * This should be called when the object is going to go out of memory.
     */
    fun unregister(lifecycle: Any) {
        //We have to remove the composition from the map, because once you unsubscribe it can't be used anymore
        val compositeSubscription = sSubscriptionsMap.remove(lifecycle)
        compositeSubscription?.clear()
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    fun publish(@Subject subject: Int, message: Any) {
        getSubject(subject).onNext(message)
    }
}