package com.example.asnova.app.di

import com.asnova.firebase.schedule.CalDavAdapteeImpl
import android.content.Context
import android.content.SharedPreferences
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.domain.repository.storage.IsAuthedUserStorage
import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.domain.repository.storage.ScheduleStateRepository
import com.asnova.domain.repository.storage.ScheduleStateStorage
import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.domain.repository.storage.ThemeSettingStorage
import com.asnova.firebase.schedule.CalDavAdapter
import com.asnova.firebase.NewsRepositoryImpl
import com.asnova.firebase.ScheduleRepositoryImpl
import com.asnova.firebase.UserRepositoryImpl
import com.asnova.firebase.api.GroupsApi
import com.asnova.storage.IsAuthedUserStorageImpl
import com.asnova.storage.LanguageSettingStorageImpl
import com.asnova.storage.NotificationsSettingStorageImpl
import com.asnova.storage.SHARED_PREFS_USER_SETTING
import com.asnova.storage.ScheduleStateRepositoryImpl
import com.asnova.storage.ScheduleStateStorageImpl
import com.asnova.storage.ThemeSettingRepositoryImpl
import com.asnova.storage.ThemeSettingStorageImpl
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    // Паттерн Singleton
    @Provides
    @Singleton
    fun provideUserSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_USER_SETTING, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        groupsApi: GroupsApi
    ): NewsRepository {
        return NewsRepositoryImpl(groupsApi)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext context: Context,
        oneTapClient: SignInClient
    ): UserRepository {
        return UserRepositoryImpl(context, oneTapClient)
    }

    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideThemeSettingRepository(themeSettingStorage: ThemeSettingStorage): ThemeSettingRepository {
        return ThemeSettingRepositoryImpl(themeSettingStorage)
    }

    @Provides
    @Singleton
    fun provideThemeSettingStorage(@ApplicationContext context: Context): ThemeSettingStorage {
        return ThemeSettingStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(): ScheduleRepository {
        // Паттерн Adapter
        val calDavAdapteeImpl = CalDavAdapteeImpl()
        val calDavAdapter = CalDavAdapter(calDavAdapteeImpl)

        // using the Adapter with Adaptee instance
        return ScheduleRepositoryImpl(calDavAdapter)
    }

    @Provides
    @Singleton
    fun provideScheduleStateStorage(@ApplicationContext context: Context): ScheduleStateStorage {
        return ScheduleStateStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideScheduleStateRepository(scheduleStateStorage: ScheduleStateStorage): ScheduleStateRepository {
        return ScheduleStateRepositoryImpl(scheduleStateStorage)
    }

    @Provides
    @Singleton
    fun provideLanguageSettingStorage(@ApplicationContext context: Context): LanguageSettingStorage {
        return LanguageSettingStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideNotificationsSettingStorage(@ApplicationContext context: Context): NotificationsSettingStorage {
        return NotificationsSettingStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideIsAuthedUserStorage(@ApplicationContext context: Context): IsAuthedUserStorage {
        return IsAuthedUserStorageImpl(context)
    }
}