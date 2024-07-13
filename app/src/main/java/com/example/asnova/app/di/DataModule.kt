package com.example.asnova.app.di

import CalDavClient
import android.content.Context
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
import com.asnova.firebase.NewsRepositoryImpl
import com.asnova.firebase.ScheduleRepositoryImpl
import com.asnova.firebase.UserRepositoryImpl
import com.asnova.storage.IsAuthedUserStorageImpl
import com.asnova.storage.LanguageSettingStorageImpl
import com.asnova.storage.NotificationsSettingStorageImpl
import com.asnova.storage.ScheduleStateRepositoryImpl
import com.asnova.storage.ScheduleStateStorageImpl
import com.asnova.storage.ThemeSettingRepositoryImpl
import com.asnova.storage.ThemeSettingStorageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideNewsRepository(): NewsRepository {
        return NewsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
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
        return ScheduleRepositoryImpl(
            CalDavClient(
                "https://calendar.mail.ru/principals/vk.com/ankudinovazaecologiy/calendars/e44497c4-4978-4518-81de-0530cf40c794/",
                "ankudinovazaecologiy@vk.com",
                "FYnERU8DZC1zvTm12NV3"
            )
        )
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