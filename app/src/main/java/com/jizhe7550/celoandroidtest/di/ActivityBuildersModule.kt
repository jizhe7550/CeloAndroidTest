package com.jizhe7550.celoandroidtest.di

import com.jizhe7550.celoandroidtest.di.user.UserFragmentBuildersModule
import com.jizhe7550.celoandroidtest.di.user.UserModule
import com.jizhe7550.celoandroidtest.di.user.UserScope
import com.jizhe7550.celoandroidtest.di.user.UserViewModelModule
import com.jizhe7550.celoandroidtest.ui.user.UserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @UserScope
    @ContributesAndroidInjector(
        modules = [UserModule::class, UserFragmentBuildersModule::class, UserViewModelModule::class]
    )
    abstract fun contributeUserActivity(): UserActivity
}