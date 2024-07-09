package com.example.asnova.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

//    @Provides
//    @Singleton
//    fun provideNewsRepository() : NewsRepository {
//        return NewsRepositoryImpl()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUserRepository() : UserRepository {
//        return UserRepositoryImpl()
//    }
//
//    @Provides
//    @Singleton
//    fun provideThemeSettingRepository(themeSettingStorage: ThemeSettingStorage) : ThemeSettingRepository {
//        return ThemeSettingRepositoryImpl(themeSettingStorage)
//    }
//
//    @Provides
//    @Singleton
//    fun provideThemeSettingStorage(@ApplicationContext context: Context) : ThemeSettingStorage {
//        return ThemeSettingStorageImpl(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideScheduleRepository() : ScheduleRepository {
//        return ScheduleRepositoryImpl()
//    }
//
//    @Provides
//    @Singleton
//    fun provideScheduleStateStorage(@ApplicationContext context: Context) : ScheduleStateStorage {
//        return ScheduleStateStorageImpl(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideScheduleStateRepository(scheduleStateStorage: ScheduleStateStorage) : ScheduleStateRepository {
//        return ScheduleStateRepositoryImpl(scheduleStateStorage)
//    }
//
//    @Provides
//    @Singleton
//    fun provideLanguageSettingStorage(@ApplicationContext context: Context) : LanguageSettingStorage {
//        return LanguageSettingStorageImpl(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideNotificationsSettingStorage(@ApplicationContext context: Context) : NotificationsSettingStorage {
//        return NotificationsSettingStorageImpl(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideIsAuthedUserStorage(@ApplicationContext context: Context) : IsAuthedUserStorage {
//        return IsAuthedUserStorageImpl(context)
//    }
}