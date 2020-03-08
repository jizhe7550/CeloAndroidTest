package com.jizhe7550.celoandroidtest.di.user

import androidx.lifecycle.ViewModel
import com.jizhe7550.celoandroidtest.di.ViewModelKey
import com.jizhe7550.celoandroidtest.ui.user.viewmodel.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

}








