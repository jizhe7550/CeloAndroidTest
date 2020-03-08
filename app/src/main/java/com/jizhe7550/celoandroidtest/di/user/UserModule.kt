package com.jizhe7550.celoandroidtest.di.user

import com.jizhe7550.celoandroidtest.api.user.UserApiService
import com.jizhe7550.celoandroidtest.persistence.AppDatabase
import com.jizhe7550.celoandroidtest.persistence.UserDao
import com.jizhe7550.celoandroidtest.repository.user.UserRepository
import com.jizhe7550.celoandroidtest.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserModule {

    @UserScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): UserApiService {
        return retrofitBuilder
            .build()
            .create(UserApiService::class.java)
    }

    @UserScope
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.getUserDao()
    }

    @UserScope
    @Provides
    fun provideUserRepository(
        userApiService: UserApiService,
        userDao: UserDao,
        sessionManager: SessionManager
    ): UserRepository {
        return UserRepository(userApiService, userDao, sessionManager)
    }
}

















