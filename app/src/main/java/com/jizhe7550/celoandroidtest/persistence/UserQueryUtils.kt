package com.jizhe7550.celoandroidtest.persistence

import androidx.lifecycle.LiveData
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_All
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_FEMALE
import com.jizhe7550.celoandroidtest.persistence.UserQueryUtils.Companion.USER_GENDER_MALE

class UserQueryUtils {


    companion object {
        private val TAG: String = "AppDebug"

        // values
        const val USER_GENDER_MALE: String = "male"
        const val USER_GENDER_FEMALE: String = "female"
        const val USER_GENDER_All: String = ""
    }
}


fun UserDao.returnUserQuery(
    query: String,
    filter: String,
    page: Int
): LiveData<List<User>> {

    return when (filter) {
        USER_GENDER_MALE, USER_GENDER_FEMALE -> searchUsersByGender(
            query = query,
            filter = filter,
            page = page
        )
        USER_GENDER_All -> searchUsers(query, page)
        else -> searchUsers(query, page)
    }
}
