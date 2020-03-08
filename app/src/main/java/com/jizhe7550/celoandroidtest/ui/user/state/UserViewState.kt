package com.jizhe7550.celoandroidtest.ui.user.state

import android.os.Parcelable
import com.jizhe7550.celoandroidtest.model.User
import kotlinx.android.parcel.Parcelize

const val USER_VIEW_STATE_BUNDLE_KEY = "com.jizhe7550.celoandroidtest.ui.user.state.UserViewState"

/**
 * User module UI states
 */
@Parcelize
data class UserViewState(

    // UserFragment vars
    var userFields: UserFields = UserFields(),

    // ViewUserFragment vars
    var viewUserFields: ViewUserFields = ViewUserFields()

): Parcelable {

    @Parcelize
    data class UserFields(
        var userList: List<User> = ArrayList(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = "",
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewUserFields(
        var user: User? = null
    ) : Parcelable
}








