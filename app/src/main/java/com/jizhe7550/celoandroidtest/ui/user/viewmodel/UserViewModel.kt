package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.jizhe7550.celoandroidtest.ui.user.state.UserStateEvent
import com.jizhe7550.celoandroidtest.ui.user.state.UserStateEvent.*
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState
import com.jizhe7550.celoandroidtest.repository.user.UserRepository
import com.jizhe7550.celoandroidtest.ui.BaseViewModel
import com.jizhe7550.celoandroidtest.ui.DataState
import com.jizhe7550.celoandroidtest.ui.Loading
import com.jizhe7550.celoandroidtest.util.PreferenceKeys.Companion.USER_FILTER
import javax.inject.Inject

class UserViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<UserStateEvent, UserViewState>() {

    init {
        setUserFilter(
            sharedPreferences.getString(
                USER_FILTER,
                ""
            )
        )
    }

    override fun handleStateEvent(stateEvent: UserStateEvent): LiveData<DataState<UserViewState>> {
        when (stateEvent) {

            is UserSearchEvent -> {
                clearLayoutManagerState()
                return userRepository.searchUsers(
                    query = getSearchQuery(),
                    filter = getFilter(),
                    page = getPage()
                )
            }

            is RestoreUserListFromCache -> {
                return userRepository.restoreUserListFromCache(
                    query = getSearchQuery(),
                    filter = getFilter(),
                    page = getPage()
                )
            }

            is None -> {
                return liveData {
                    emit(
                        DataState(
                            null,
                            Loading(false),
                            null
                        )
                    )
                }
            }
        }
    }

    override fun initNewViewState(): UserViewState {
        return UserViewState()
    }

    fun saveFilterOptions(filter: String) {
        editor.putString(USER_FILTER, filter)
        editor.apply()
    }

    fun cancelActiveJobs() {
        userRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    private fun handlePendingData() {
        setStateEvent(None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
        Log.d(TAG, "CLEARED...")
    }


}











