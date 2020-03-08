package com.jizhe7550.celoandroidtest.ui.user.viewmodel

import com.jizhe7550.celoandroidtest.model.User


fun UserViewModel.getFilter(): String {
    getCurrentViewStateOrNew().let {
        return it.userFields.filter
    }
}

fun UserViewModel.getSearchQuery(): String {
    getCurrentViewStateOrNew().let {
        return it.userFields.searchQuery
    }
}

fun UserViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.userFields.page
    }
}


fun UserViewModel.getUser(): User {
    getCurrentViewStateOrNew().let { userViewState ->
        return userViewState.viewUserFields.user?.let {
            return it
        }?: getDummyUser()
    }
}

fun UserViewModel.getDummyUser(): User {
    return User("","","","","" ,"" ,"","","","",0,1)
}








