package com.example.asnova.app.di

import com.example.asnova.screen.main.feed.api.GroupsApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideGroupsApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): GroupsApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .baseUrl("https://api.vk.com/method/")
        .build().create(GroupsApi::class.java)

    @Provides
    fun provideWallId(): Int {
        return 221091451
    }

    @Provides
    fun provideAccessToken(): String {
        return "2c7485642c7485642c748564202f6dcfcc22c742c7485644afaf2742c0714f09e3fa61a"
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        Json { ignoreUnknownKeys = true; }.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()
}
