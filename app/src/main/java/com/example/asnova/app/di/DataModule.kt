//package com.example.asnova.app.di
//
//import android.content.Context
//import com.elnfach.data.implementations.ScheduleStateRepositoryImpl
//import com.elnfach.data.implementations.ThemeSettingRepositoryImpl
//import com.elnfach.domain.repository.firebase.NewsRepository
//import com.elnfach.domain.repository.firebase.ScheduleRepository
//import com.elnfach.domain.repository.firebase.UserRepository
//import com.elnfach.domain.repository.storage.IsAuthedUserStorage
//import com.elnfach.domain.repository.storage.ScheduleStateRepository
//import com.elnfach.firebase.NewsRepositoryImpl
//import com.elnfach.firebase.ScheduleRepositoryImpl
//import com.elnfach.firebase.UserRepositoryImpl
//import com.elnfach.storage.IsAuthedUserStorageImpl
//import com.elnfach.storage.LanguageSettingStorageImpl
//import com.elnfach.storage.NotificationsSettingStorageImpl
//import com.elnfach.storage.ScheduleStateStorageImpl
//import com.elnfach.storage.ThemeSettingStorageImpl
//import com.elnfach.storage.repository.LanguageSettingStorage
//import com.elnfach.storage.repository.NotificationsSettingStorage
//import com.elnfach.storage.repository.ScheduleStateStorage
//import com.elnfach.storage.repository.ThemeSettingRepository
//import com.elnfach.domain.repository.storage.ThemeSettingStorage
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class DataModule {
//
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
//}