package com.eselman.cities.challenge.di

import com.eselman.cities.challenge.BuildConfig
import com.eselman.cities.challenge.network.services.CitiesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideBaseUrl(): String = BuildConfig.CITIES_API_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesCitiesApiService(retrofit: Retrofit): CitiesApiService = retrofit.create(
        CitiesApiService::class.java
    )
}
