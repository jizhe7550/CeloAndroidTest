package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.util.Log
import com.jizhe7550.celoandroidtest.ui.user.state.UserStateEvent.*
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState

/**
 * manage UserViewModel pagination behaviors
 */
fun UserViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.userFields.page = 1
    setViewState(update)
}

fun UserViewModel.refreshFromCache(){
    setQueryInProgress(true)
    setQueryExhausted(false)
    setStateEvent(RestoreUserListFromCache)
}

fun UserViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(UserSearchEvent)
    Log.e(TAG, "UserViewModel: loadFirstPage: ${viewState.value!!.userFields.searchQuery}")
}

private fun UserViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().userFields.page // get current page
    update.userFields.page = page + 1
    setViewState(update)
}

fun UserViewModel.nextPage(){
    if(!viewState.value!!.userFields.isQueryInProgress
        && !viewState.value!!.userFields.isQueryExhausted){
        Log.d(TAG, "UserViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(UserSearchEvent)
    }
}

fun UserViewModel.handleIncomingUserListData(viewState: UserViewState){
    Log.d(TAG, "UserViewModel, DataState: ${viewState}")
    Log.d(TAG, "UserViewModel, DataState: isQueryInProgress?: " +
            "${viewState.userFields.isQueryInProgress}")
    Log.d(TAG, "UserViewModel, DataState: isQueryExhausted?: " +
            "${viewState.userFields.isQueryExhausted}")
    setQueryInProgress(viewState.userFields.isQueryInProgress)
    setQueryExhausted(viewState.userFields.isQueryExhausted)
    setUserListData(viewState.userFields.userList)
}


