package com.jizhe7550.celoandroidtest.ui.user.state


sealed class UserStateEvent {

    object UserSearchEvent : UserStateEvent()

    object RestoreUserListFromCache : UserStateEvent()

    object None : UserStateEvent()
}