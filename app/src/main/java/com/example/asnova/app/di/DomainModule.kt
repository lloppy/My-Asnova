package com.example.asnova.app.di

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.domain.repository.storage.IsAuthedUserStorage
import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.domain.repository.storage.ScheduleStateRepository
import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.domain.usecase.CheckIsAdminUseCase
import com.asnova.domain.usecase.CheckUserClassUseCase
import com.asnova.domain.usecase.CheckUserDataUseCase
import com.asnova.domain.usecase.CleanAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.CreateUserWithPhoneUseCase
import com.asnova.domain.usecase.DeleteAccountUseCase
import com.asnova.domain.usecase.GetAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.GetAsnovaNewsUseCase
import com.asnova.domain.usecase.GetIsAuthedUserUseCase
import com.asnova.domain.usecase.GetLanguageSettingUseCase
import com.asnova.domain.usecase.GetNewsByOrderUseCase
import com.asnova.domain.usecase.GetNewsItemByIdUseCase
import com.asnova.domain.usecase.GetNotificationsSettingUseCase
import com.asnova.domain.usecase.GetRawAsnovaClassesUseCase
import com.asnova.domain.usecase.GetSafetyNewsUseCase
import com.asnova.domain.usecase.GetScheduleFromSiteUseCase
import com.asnova.domain.usecase.GetScheduleMapUseCase
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.GetThemeSettingUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.IsAuthedUserUseCase
import com.asnova.domain.usecase.OnDownloadMoreAsnovaNewsUseCase
import com.asnova.domain.usecase.OnDownloadMoreSafetyNewsUseCase
import com.asnova.domain.usecase.PushAsnovaClassesUseCase
import com.asnova.domain.usecase.SaveAuthStatusUseCase
import com.asnova.domain.usecase.SaveLanguageSettingUseCase
import com.asnova.domain.usecase.SaveNotificationsSettingUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.domain.usecase.SaveThemeSettingUseCase
import com.asnova.domain.usecase.SelectClassUseCase
import com.asnova.domain.usecase.SignInWithIntentUseCase
import com.asnova.domain.usecase.SignInWithLauncher
import com.asnova.domain.usecase.SignInWithOtpUseCase
import com.asnova.domain.usecase.SignInWithPhoneUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.asnova.domain.usecase.SubmitPromocodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun providePushAsnovaClassesUseCase(
        repository: ScheduleRepository
    ): PushAsnovaClassesUseCase {
        return PushAsnovaClassesUseCase(repository)
    }

    @Provides
    fun provideCheckUserClassUseCase(
        repository: UserRepository
    ): CheckUserClassUseCase {
        return CheckUserClassUseCase(repository)
    }

    @Provides
    fun provideSignInWithPhoneUseCase(
        repository: UserRepository
    ): SignInWithPhoneUseCase {
        return SignInWithPhoneUseCase(repository)
    }

    @Provides
    fun provideGetAsnovaClassesUseCase(
        scheduleRepository: ScheduleRepository
    ): GetRawAsnovaClassesUseCase {
        return GetRawAsnovaClassesUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetUserDataUseCase(
        userRepository: UserRepository
    ): GetUserDataUseCase {
        return GetUserDataUseCase(userRepository)
    }

    @Provides
    fun provideSignInWithIntentUseCase(
        userRepository: UserRepository
    ): SignInWithIntentUseCase {
        return SignInWithIntentUseCase(userRepository)
    }

    @Provides
    fun provideCreateUserWithPhoneUseCase(
        userRepository: UserRepository
    ): CreateUserWithPhoneUseCase {
        return CreateUserWithPhoneUseCase(userRepository)
    }

    @Provides
    fun provideSignInWithOtpUseCase(
        userRepository: UserRepository
    ): SignInWithOtpUseCase {
        return SignInWithOtpUseCase(userRepository)
    }

    @Provides
    fun provideSignInWithLauncherUseCase(
        userRepository: UserRepository
    ): SignInWithLauncher {
        return SignInWithLauncher(userRepository)
    }

    @Provides
    fun provideGetLanguageSettingUseCase(
        languageSettingStorage: LanguageSettingStorage
    ): GetLanguageSettingUseCase {
        return GetLanguageSettingUseCase(languageSettingStorage)
    }

    @Provides
    fun provideGetNewsByOrderUseCase(
        newsRepository: NewsRepository
    ): GetNewsByOrderUseCase {
        return GetNewsByOrderUseCase(newsRepository)
    }

    @Provides
    fun provideCheckIsAdminUseCase(
        userRepository: UserRepository
    ): CheckIsAdminUseCase {
        return CheckIsAdminUseCase(userRepository)
    }

    @Provides
    fun provideCheckUserDataUseCase(
        userRepository: UserRepository
    ): CheckUserDataUseCase {
        return CheckUserDataUseCase(userRepository)
    }

    @Provides
    fun provideSubmitPromocodeUseCase(
        userRepository: UserRepository
    ): SubmitPromocodeUseCase {
        return SubmitPromocodeUseCase(userRepository)
    }

    @Provides
    fun provideDeleteAccountUseCase(
        userRepository: UserRepository
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase(userRepository)
    }

    @Provides
    fun provideSelectClassUseCase(
        userRepository: UserRepository
    ): SelectClassUseCase {
        return SelectClassUseCase(userRepository)
    }

    @Provides
    fun provideCleanAsnovaClassesFromFirebaseUseCase(
        scheduleRepository: ScheduleRepository
    ): CleanAsnovaClassesFromFirebaseUseCase {
        return CleanAsnovaClassesFromFirebaseUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetAsnovaClassesFromFirebaseUseCase(
        scheduleRepository: ScheduleRepository
    ): GetAsnovaClassesFromFirebaseUseCase {
        return GetAsnovaClassesFromFirebaseUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetSafetyNewsUseCase(
        newsRepository: NewsRepository
    ): GetSafetyNewsUseCase {
        return GetSafetyNewsUseCase(newsRepository)
    }

    @Provides
    fun provideGetAsnovaNewsUseCase(
        newsRepository: NewsRepository
    ): GetAsnovaNewsUseCase {
        return GetAsnovaNewsUseCase(newsRepository)
    }

    @Provides
    fun provideGetNewsItemByIdUseCase(
        newsRepository: NewsRepository
    ): GetNewsItemByIdUseCase {
        return GetNewsItemByIdUseCase(newsRepository)
    }

    @Provides
    fun provideOnDownloadMoreAsnovaNewsUseCase(
        newsRepository: NewsRepository
    ): OnDownloadMoreAsnovaNewsUseCase {
        return OnDownloadMoreAsnovaNewsUseCase(newsRepository)
    }

    @Provides
    fun provideOnDownloadMoreSafetyNewsUseCase(
        newsRepository: NewsRepository
    ): OnDownloadMoreSafetyNewsUseCase {
        return OnDownloadMoreSafetyNewsUseCase(newsRepository)
    }

    @Provides
    fun provideGetNotificationsSettingUseCase(
        notificationsSettingStorage: NotificationsSettingStorage
    ): GetNotificationsSettingUseCase {
        return GetNotificationsSettingUseCase(notificationsSettingStorage)
    }

    @Provides
    fun provideGetScheduleStateUseCase(
        scheduleStateRepository: ScheduleStateRepository
    ): GetScheduleStateUseCase {
        return GetScheduleStateUseCase(scheduleStateRepository)
    }

    @Provides
    fun provideGetScheduleUseCase(
        scheduleRepository: ScheduleRepository
    ): GetScheduleUseCase {
        return GetScheduleUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetMapScheduleUseCase(
        scheduleRepository: ScheduleRepository
    ): GetScheduleMapUseCase {
        return GetScheduleMapUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetScheduleFromSiteUseCase(
        scheduleRepository: ScheduleRepository
    ): GetScheduleFromSiteUseCase {
        return GetScheduleFromSiteUseCase(scheduleRepository)
    }

    @Provides
    fun provideGetThemeSettingUseCase(
        themeSettingRepository: ThemeSettingRepository
    ): GetThemeSettingUseCase {
        return GetThemeSettingUseCase(themeSettingRepository)
    }

    @Provides
    fun provideIsAuthedUserUseCase(
        userRepository: UserRepository
    ): IsAuthedUserUseCase {
        return IsAuthedUserUseCase(userRepository)
    }

    @Provides
    fun provideSaveLanguageSettingUseCase(
        languageSettingStorage: LanguageSettingStorage
    ): SaveLanguageSettingUseCase {
        return SaveLanguageSettingUseCase(languageSettingStorage)
    }

    @Provides
    fun provideSaveNotificationsSettingUseCase(
        notificationsSettingStorage: NotificationsSettingStorage
    ): SaveNotificationsSettingUseCase {
        return SaveNotificationsSettingUseCase(notificationsSettingStorage)
    }

    @Provides
    fun provideSaveScheduleStateUseCase(
        scheduleStateRepository: ScheduleStateRepository
    ): SaveScheduleStateUseCase {
        return SaveScheduleStateUseCase(scheduleStateRepository)
    }

    @Provides
    fun provideSaveThemeSettingUseCase(
        themeSettingRepository: ThemeSettingRepository
    ): SaveThemeSettingUseCase {
        return SaveThemeSettingUseCase(themeSettingRepository)
    }

    @Provides
    fun provideSignOutUserUseCase(
        userRepository: UserRepository
    ): SignOutUserUseCase {
        return SignOutUserUseCase(userRepository)
    }

    @Provides
    fun provideGetIsAuthedUserUseCase(
        isAuthedUserStorage: IsAuthedUserStorage
    ): GetIsAuthedUserUseCase {
        return GetIsAuthedUserUseCase(isAuthedUserStorage)
    }

    @Provides
    fun provideSetIsAuthedUserUseCase(
        isAuthedUserStorage: IsAuthedUserStorage
    ): SaveAuthStatusUseCase {
        return SaveAuthStatusUseCase(isAuthedUserStorage)
    }

}