package com.example.asnova.app.di

import android.content.Context
import com.example.asnova.screen.main.feed.api.FileUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFileUtils(@ApplicationContext context: Context): FileUtils = FileUtils(context)

}