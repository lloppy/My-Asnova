//package com.example.asnova.app.di
//
//import com.elnfach.domain.repository.firebase.NewsRepository
//import com.elnfach.domain.repository.firebase.ScheduleRepository
//import com.elnfach.domain.repository.firebase.UserRepository
//import com.elnfach.domain.repository.storage.IsAuthedUserStorage
//import com.elnfach.domain.repository.storage.ScheduleStateRepository
//import com.elnfach.domain.usecase.AddNewLessonUseCase
//import com.elnfach.domain.usecase.AddNewsArticleUseCase
//import com.elnfach.domain.usecase.AddNewsItemToFavoritesUseCase
//import com.elnfach.domain.usecase.CreateUserWithEmailAndPasswordUseCase
//import com.elnfach.domain.usecase.GetAllFavoritesUseCase
//import com.elnfach.domain.usecase.GetIsAuthedUserUseCase
//import com.elnfach.domain.usecase.GetLanguageSettingUseCase
//import com.elnfach.domain.usecase.GetNewsByOrderUseCase
//import com.elnfach.domain.usecase.GetNewsItemByIdUseCase
//import com.elnfach.domain.usecase.GetNotificationsSettingUseCase
//import com.elnfach.domain.usecase.GetScheduleStateUseCase
//import com.elnfach.domain.usecase.GetScheduleUseCase
//import com.elnfach.domain.usecase.GetThemeSettingUseCase
//import com.elnfach.domain.usecase.IsAuthedUserUseCase
//import com.elnfach.domain.usecase.PullRequestUserUseCase
//import com.elnfach.domain.usecase.SaveAuthStatusUseCase
//import com.elnfach.domain.usecase.SaveLanguageSettingUseCase
//import com.elnfach.domain.usecase.SaveNotificationsSettingUseCase
//import com.elnfach.domain.usecase.SaveScheduleStateUseCase
//import com.elnfach.domain.usecase.SaveThemeSettingUseCase
//import com.elnfach.domain.usecase.SignInUserWithEmailAndPasswordUseCase
//import com.elnfach.domain.usecase.SignOutUserUseCase
//import com.elnfach.storage.repository.LanguageSettingStorage
//import com.elnfach.storage.repository.NotificationsSettingStorage
//import com.elnfach.storage.repository.ThemeSettingRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//
//@Module
//@InstallIn(ViewModelComponent::class)
//class DomainModule {
//
//    @Provides
//    fun provideAddNewsArticleUseCase(
//        repository: NewsRepository
//    ) : AddNewsArticleUseCase {
//        return AddNewsArticleUseCase(repository)
//    }
//
//    @Provides
//    fun provideCreateUserWithEmailAndPasswordUseCase(
//        repository: UserRepository
//    ) : CreateUserWithEmailAndPasswordUseCase {
//        return CreateUserWithEmailAndPasswordUseCase(repository)
//    }
//
//    @Provides
//    fun provideGetLanguageSettingUseCase(
//        languageSettingStorage: LanguageSettingStorage
//    ) : GetLanguageSettingUseCase {
//        return GetLanguageSettingUseCase(languageSettingStorage)
//    }
//
//    @Provides
//    fun provideGetNewsByOrderUseCase(
//        repository: NewsRepository
//    ) : GetNewsByOrderUseCase {
//        return GetNewsByOrderUseCase(repository)
//    }
//
//    @Provides
//    fun provideGetNewsItemByIdUseCase(
//        repository: NewsRepository
//    ) : GetNewsItemByIdUseCase {
//        return GetNewsItemByIdUseCase(repository)
//    }
//
//    @Provides
//    fun provideGetNotificationsSettingUseCase(
//        notificationsSettingStorage: NotificationsSettingStorage
//    ) : GetNotificationsSettingUseCase {
//        return GetNotificationsSettingUseCase(notificationsSettingStorage)
//    }
//
//    @Provides
//    fun provideGetScheduleStateUseCase(
//        scheduleStateRepository: ScheduleStateRepository
//    ) : GetScheduleStateUseCase {
//        return GetScheduleStateUseCase(scheduleStateRepository)
//    }
//
//    @Provides
//    fun provideGetScheduleUseCase(
//        scheduleRepository: ScheduleRepository
//    ) : GetScheduleUseCase {
//        return GetScheduleUseCase(scheduleRepository)
//    }
//
//    @Provides
//    fun provideGetThemeSettingUseCase(
//        themeSettingRepository: ThemeSettingRepository
//    ) : GetThemeSettingUseCase {
//        return GetThemeSettingUseCase(themeSettingRepository)
//    }
//
//    @Provides
//    fun provideIsAuthedUserUseCase(
//        userRepository: UserRepository
//    ) : IsAuthedUserUseCase {
//        return IsAuthedUserUseCase(userRepository)
//    }
//
//    @Provides
//    fun provideSaveLanguageSettingUseCase(
//        languageSettingStorage: LanguageSettingStorage
//    ) : SaveLanguageSettingUseCase {
//        return SaveLanguageSettingUseCase(languageSettingStorage)
//    }
//
//    @Provides
//    fun provideSaveNotificationsSettingUseCase(
//        notificationsSettingStorage: NotificationsSettingStorage
//    ) : SaveNotificationsSettingUseCase {
//        return SaveNotificationsSettingUseCase(notificationsSettingStorage)
//    }
//
//    @Provides
//    fun provideSaveScheduleStateUseCase(
//        scheduleStateRepository: ScheduleStateRepository
//    ) : SaveScheduleStateUseCase {
//        return SaveScheduleStateUseCase(scheduleStateRepository)
//    }
//
//    @Provides
//    fun provideSaveThemeSettingUseCase(
//        themeSettingRepository: ThemeSettingRepository
//    ) : SaveThemeSettingUseCase {
//        return SaveThemeSettingUseCase(themeSettingRepository)
//    }
//
//    @Provides
//    fun provideSignInUserWithEmailAndPasswordUseCase(
//        userRepository: UserRepository
//    ) : SignInUserWithEmailAndPasswordUseCase {
//        return SignInUserWithEmailAndPasswordUseCase(userRepository)
//    }
//
//    @Provides
//    fun provideSignOutUserUseCase(
//        userRepository: UserRepository
//    ) : SignOutUserUseCase {
//        return SignOutUserUseCase(userRepository)
//    }
//
//    @Provides
//    fun provideGetIsAuthedUserUseCase(
//        isAuthedUserStorage: IsAuthedUserStorage
//    ) : GetIsAuthedUserUseCase{
//        return GetIsAuthedUserUseCase(isAuthedUserStorage)
//    }
//
//    @Provides
//    fun provideSetIsAuthedUserUseCase(
//        isAuthedUserStorage: IsAuthedUserStorage
//    ) : SaveAuthStatusUseCase {
//        return SaveAuthStatusUseCase(isAuthedUserStorage)
//    }
//
//    @Provides
//    fun providePullRequestUserUseCase(
//        userRepository: UserRepository
//    ) : PullRequestUserUseCase {
//        return PullRequestUserUseCase(userRepository)
//    }
//
//    @Provides
//    fun provideAddNewLessonUseCase(
//        scheduleRepository: ScheduleRepository
//    ) : AddNewLessonUseCase {
//        return AddNewLessonUseCase(scheduleRepository)
//    }
//
//    @Provides
//    fun provideAddNewsItemToFavoritesUseCase(
//        userRepository: UserRepository
//    ) : AddNewsItemToFavoritesUseCase {
//        return AddNewsItemToFavoritesUseCase(userRepository)
//    }
//
//    @Provides
//    fun provideGetAllFavoritesUseCase(
//        newsRepository: NewsRepository,
//        userRepository: UserRepository
//    ) : GetAllFavoritesUseCase {
//        return GetAllFavoritesUseCase(newsRepository, userRepository)
//    }
//}