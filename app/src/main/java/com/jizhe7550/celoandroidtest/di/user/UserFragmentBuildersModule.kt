package com.jizhe7550.celoandroidtest.di.user

import com.jizhe7550.celoandroidtest.ui.user.viewmodel.UserFragment
import com.jizhe7550.celoandroidtest.ui.user.viewmodel.ViewUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewUserFragment(): ViewUserFragment

}