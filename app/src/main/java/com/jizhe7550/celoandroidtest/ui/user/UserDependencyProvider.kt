package com.jizhe7550.celoandroidtest.ui.user

import com.bumptech.glide.RequestManager
import com.jizhe7550.celoandroidtest.viewmodelutil.ViewModelProviderFactory

/**
 * Provides app-level dependencies to various BaseFragments:
 * eg BaseUserFragment
 *
 * Must do this because of process death issue and restoring state.
 * Why?
 * Can't set values that were saved in instance state to ViewModel because Viewmodel
 * hasn't been created yet when onCreate is called.
 */
interface UserDependencyProvider{

    fun getVMProviderFactory(): ViewModelProviderFactory

    fun getGlideRequestManager(): RequestManager
}