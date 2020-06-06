

package com.biki.quickmeal.injection.module

import com.biki.quickmeal.injection.annotations.RunOn
import com.biki.quickmeal.schedulers.SchedulerType
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Provides common Schedulers used by RxJava
 */

@Module
class SchedulerModule {

    @Provides
    @RunOn(SchedulerType.IO)
    internal fun provideIo(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @RunOn(SchedulerType.COMPUTATION)
    internal fun provideComputation(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    @RunOn(SchedulerType.UI)
    internal fun provideUi(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}