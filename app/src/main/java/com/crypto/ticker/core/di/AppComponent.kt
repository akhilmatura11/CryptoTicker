package com.crypto.ticker.core.di

import android.content.Context
import com.crypto.ticker.api.ApiInterface
import com.crypto.ticker.data.local.database.CoinsDatabase
import com.crypto.ticker.data.local.prefs.PreferenceStorage
import com.crypto.ticker.data.local.prefs.SharedPreferenceStorage
import com.crypto.ticker.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
                .addHeader("x-cg-demo-api-key", "CG-5WP9jSr2uvKAdbZ9xFbJgsTT")

            val httpUrl = request.url().newBuilder()
            val interceptor = builder.url(httpUrl.build()).build()

            return@Interceptor chain.proceed(interceptor)
        }

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): CoinsDatabase =
        CoinsDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

}