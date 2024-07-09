package com.example.asnova.app.di

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.domain.repository.storage.IsAuthedUserStorage
import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.domain.repository.storage.ScheduleStateRepository
import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.domain.usecase.AddNewLessonUseCase
import com.asnova.domain.usecase.AddNewsArticleUseCase
import com.asnova.domain.usecase.AddNewsItemToFavoritesUseCase
import com.asnova.domain.usecase.CreateUserWithEmailAndPasswordUseCase
import com.asnova.domain.usecase.GetAllFavoritesUseCase
import com.asnova.domain.usecase.GetIsAuthedUserUseCase
import com.asnova.domain.usecase.GetLanguageSettingUseCase
import com.asnova.domain.usecase.GetNewsByOrderUseCase
import com.asnova.domain.usecase.GetNewsItemByIdUseCase
import com.asnova.domain.usecase.GetNotificationsSettingUseCase
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.GetThemeSettingUseCase
import com.asnova.domain.usecase.IsAuthedUserUseCase
import com.asnova.domain.usecase.PullRequestUserUseCase
import com.asnova.domain.usecase.SaveAuthStatusUseCase
import com.asnova.domain.usecase.SaveLanguageSettingUseCase
import com.asnova.domain.usecase.SaveNotificationsSettingUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.domain.usecase.SaveThemeSettingUseCase
import com.asnova.domain.usecase.SignInUserWithEmailAndPasswordUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideAddNewsArticleUseCase(
        repository: NewsRepository
    ): AddNewsArticleUseCase {
        return AddNewsArticleUseCase(repository)
    }

    @Provides
    fun provideCreateUserWithEmailAndPasswordUseCase(
        repository: UserRepository
    ): CreateUserWithEmailAndPasswordUseCase {
        return CreateUserWithEmailAndPasswordUseCase(repository)
    }

    @Provides
    fun provideGetLanguageSettingUseCase(
        languageSettingStorage: LanguageSettingStorage
    ): GetLanguageSettingUseCase {
        return GetLanguageSettingUseCase(languageSettingStorage)
    }

    @Provides
    fun provideGetNewsByOrderUseCase(
        repository: NewsRepository
    ): GetNewsByOrderUseCase {
        return GetNewsByOrderUseCase(repository)
    }

    @Provides
    fun provideGetNewsItemByIdUseCase(
        repository: NewsRepository
    ): GetNewsItemByIdUseCase {
        return GetNewsItemByIdUseCase(repository)
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
    fun provideSignInUserWithEmailAndPasswordUseCase(
        userRepository: UserRepository
    ): SignInUserWithEmailAndPasswordUseCase {
        return SignInUserWithEmailAndPasswordUseCase(userRepository)
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

    @Provides
    fun providePullRequestUserUseCase(
        userRepository: UserRepository
    ): PullRequestUserUseCase {
        return PullRequestUserUseCase(userRepository)
    }

    @Provides
    fun provideAddNewLessonUseCase(
        scheduleRepository: ScheduleRepository
    ): AddNewLessonUseCase {
        return AddNewLessonUseCase(scheduleRepository)
    }

    @Provides
    fun provideAddNewsItemToFavoritesUseCase(
        userRepository: UserRepository
    ): AddNewsItemToFavoritesUseCase {
        return AddNewsItemToFavoritesUseCase(userRepository)
    }

    @Provides
    fun provideGetAllFavoritesUseCase(
        newsRepository: NewsRepository,
        userRepository: UserRepository
    ): GetAllFavoritesUseCase {
        return GetAllFavoritesUseCase(newsRepository, userRepository)
    }
}