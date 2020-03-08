package com.jizhe7550.celoandroidtest.di

import androidx.lifecycle.ViewModelProvider
import com.jizhe7550.celoandroidtest.viewmodelutil.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}