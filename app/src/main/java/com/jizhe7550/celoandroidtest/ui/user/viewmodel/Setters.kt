package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import android.os.Parcelable
import com.jizhe7550.celoandroidtest.model.User

fun UserViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.userFields.searchQuery = query
    setViewState(update)
}

fun UserViewModel.setBlogListData(userList: List<User>){
    val update = getCurrentViewStateOrNew()
    update.userFields.userList = userList
    setViewState(update)
}

fun UserViewModel.setUser(user: User){
    val update = getCurrentViewStateOrNew()
    update.viewUserFields.user = user
    setViewState(update)
}

fun UserViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.userFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun UserViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.userFields.isQueryInProgress = isInProgress
    setViewState(update)
}


// Filter can be male or female
fun UserViewModel.setUserFilter(filter: String?){
    filter?.let{
        val update = getCurrentViewStateOrNew()
        update.userFields.filter = filter
        setViewState(update)
    }
}

fun UserViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.userFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

fun UserViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.userFields.layoutManagerState = null
    setViewState(update)
}






