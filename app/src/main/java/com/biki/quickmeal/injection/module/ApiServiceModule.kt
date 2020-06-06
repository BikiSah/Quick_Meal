

package com.biki.quickmeal.injection.module


import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.biki.quickmeal.BuildConfig
import com.biki.quickmeal.QuickMealApplication
import com.biki.quickmeal.data.api.QuickMealService
import com.biki.quickmeal.data.api.HeaderInterceptor
import com.biki.quickmeal.extensions.isConnected
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton



@Module
open class ApiServiceModule {

    @Provides
    @Named(BASE_URL)
    internal fun provideBaseUrl(): String {
        return BuildConfig.API_URL
    }

    @Provides
    @Singleton
    internal fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(
        headerInterceptor: HeaderInterceptor,
        httpInterceptor: HttpLoggingInterceptor, app: QuickMealApplication
    ): OkHttpClient {

        val cacheSize = (10 * 1024 * 1024).toLong()
        val httpCacheDirectory = File(app.cacheDir, "Response")
        val myCache = Cache(httpCacheDirectory, cacheSize)

        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(myCache)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
            .addInterceptor(httpInterceptor.apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })

        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        return okHttpClientBuilder
            .addInterceptor { chain ->

                var request: Request = chain.request()
                if (!connectivityManager.isConnected) {
                    Log.d("Retrofit", "No Internet")
                    request = request.newBuilder().removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached").build()
                }
                chain.proceed(request)
            }
            .build()
    }

    private val REWRITE_RESPONSE_INTERCEPTOR: Interceptor = Interceptor { chain ->
        val originalResponse: Response = chain.proceed(chain.request())
        val cacheControl = originalResponse.header("Cache-Control")
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                "no-cache"
            ) ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
        ) {
            originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 5000)
                .build()
        } else {
            originalResponse
        }
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRxJavaAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        @Named(BASE_URL) baseUrl: String, converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory, client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(client)
            .build()
    }

    companion object {
        private const val BASE_URL = "base_url"
    }

    /* Specific services */
    @Provides
    @Singleton
    open fun provideService(retrofit: Retrofit): QuickMealService {
        return retrofit.create(QuickMealService::class.java)
    }
}
