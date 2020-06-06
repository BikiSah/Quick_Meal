

package com.biki.quickmeal.injection


import com.biki.quickmeal.injection.module.*
import dagger.Component
import javax.inject.Singleton



@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class, ApiServiceModule::class, SchedulerModule::class])
interface AppComponent {

    fun activityModule(activityModule: ActivityModule): ActivityComponent


}