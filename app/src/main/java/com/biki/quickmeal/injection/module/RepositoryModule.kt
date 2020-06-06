

package com.biki.quickmeal.injection.module

import com.biki.quickmeal.data.repository.QuickMealDataSource
import com.biki.quickmeal.data.repository.local.QuickMealLocalDataSource
import com.biki.quickmeal.data.repository.remote.QuickMealRemoteDataSource
import com.biki.quickmeal.injection.annotations.Local
import com.biki.quickmeal.injection.annotations.Remote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Local
    fun providesLocalDataSource(localDataSource: QuickMealLocalDataSource): QuickMealDataSource = localDataSource

    @Provides
    @Singleton
    @Remote
    fun providesRemoteDataSource(remoteDataSource: QuickMealRemoteDataSource): QuickMealDataSource = remoteDataSource

}