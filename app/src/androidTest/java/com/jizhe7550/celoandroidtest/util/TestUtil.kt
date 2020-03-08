package com.jizhe7550.celoandroidtest.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.jizhe7550.celoandroidtest.api.user.responses.UserListSearchResponse
import com.jizhe7550.celoandroidtest.api.user.responses.UserSearchResponse
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.ui.Data
import com.jizhe7550.celoandroidtest.ui.DataState
import com.jizhe7550.celoandroidtest.ui.Event
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState


object TestUtil {


    val TEST_USER_1 = User(
        "ji.zhe17550@gmail.com",
        "02102010201",
        "02102010201",
        "Mr",
        "https://randomuser.me/api/portraits/thumb/men/97.jpg",
        "https://randomuser.me/api/portraits/med/men/97.jpg",
        "https://randomuser.me/api/portraits/men/97.jpg",
        "male",
        "aa1",
        "bbb1",
        20,
        471697200000
    )

    val TEST_USER_2 = User(
        "Sosan.Li7550@gmail.com",
        "02102010201",
        "02102010201",
        "Mrs",
        "https://randomuser.me/api/portraits/thumb/women/10.jpg",
        "https://randomuser.me/api/portraits/med/women/10.jpg",
        "https://randomuser.me/api/portraits/women/10.jpg",
        "female",
        "aa2",
        "bbb2",
        30,
        471697200000
    )

    val TEST_USER_3 = User(
        "Joan.Li7550@gmail.com",
        "02102010201",
        "02102010201",
        "Mrs",
        "https://randomuser.me/api/portraits/thumb/women/10.jpg",
        "https://randomuser.me/api/portraits/med/women/10.jpg",
        "https://randomuser.me/api/portraits/women/10.jpg",
        "female",
        "cc1",
        "ddd1",
        27,
        471697200000
    )

    val TEST_USER_4 = User(
        "Joe.Li7550@gmail.com",
        "02102010201",
        "02102010201",
        "Mr",
        "https://randomuser.me/api/portraits/thumb/men/97.jpg",
        "https://randomuser.me/api/portraits/med/men/97.jpg",
        "https://randomuser.me/api/portraits/men/97.jpg",
        "male",
        "cc2",
        "ddd2",
        22,
        471697200000
    )

    val USER_SEARCH_RESPONSE_1 = UserSearchResponse(
        UserSearchResponse.Dob(
            TEST_USER_1.age,
            DateUtils.convertLongToStringDate(TEST_USER_1.date)
        ),
        TEST_USER_1.gender,
        TEST_USER_1.email,
        UserSearchResponse.Name(TEST_USER_1.title, TEST_USER_1.first, TEST_USER_1.last),
        UserSearchResponse.Picture(TEST_USER_1.large, TEST_USER_1.medium, TEST_USER_1.thumbnail),
        TEST_USER_1.phone,
        TEST_USER_1.cell
    )

    val USER_SEARCH_RESPONSE_2 = UserSearchResponse(
        UserSearchResponse.Dob(
            TEST_USER_2.age,
            DateUtils.convertLongToStringDate(TEST_USER_2.date)
        ),
        TEST_USER_2.gender,
        TEST_USER_2.email,
        UserSearchResponse.Name(TEST_USER_2.title, TEST_USER_2.first, TEST_USER_2.last),
        UserSearchResponse.Picture(TEST_USER_2.large, TEST_USER_2.medium, TEST_USER_2.thumbnail),
        TEST_USER_2.phone,
        TEST_USER_2.cell
    )

    val USER_SEARCH_RESPONSE_3 = UserSearchResponse(
        UserSearchResponse.Dob(
            TEST_USER_3.age,
            DateUtils.convertLongToStringDate(TEST_USER_3.date)
        ),
        TEST_USER_3.gender,
        TEST_USER_3.email,
        UserSearchResponse.Name(TEST_USER_3.title, TEST_USER_3.first, TEST_USER_3.last),
        UserSearchResponse.Picture(TEST_USER_3.large, TEST_USER_3.medium, TEST_USER_3.thumbnail),
        TEST_USER_3.phone,
        TEST_USER_3.cell
    )

    val USER_SEARCH_RESPONSE_4 = UserSearchResponse(
        UserSearchResponse.Dob(
            TEST_USER_4.age,
            DateUtils.convertLongToStringDate(TEST_USER_4.date)
        ),
        TEST_USER_4.gender,
        TEST_USER_4.email,
        UserSearchResponse.Name(TEST_USER_4.title, TEST_USER_4.first, TEST_USER_4.last),
        UserSearchResponse.Picture(TEST_USER_4.large, TEST_USER_4.medium, TEST_USER_4.thumbnail),
        TEST_USER_4.phone,
        TEST_USER_4.cell
    )

    val USER_LIST_SEARCH_RESPONSE_1 =
        UserListSearchResponse(arrayListOf(USER_SEARCH_RESPONSE_1), "")

    fun createDataStateWrapViewState(): DataState<UserViewState> {
        val userList = arrayListOf(TEST_USER_1)
        val userViewState = UserViewState(
            UserViewState.UserFields(
                userList = userList,
                isQueryInProgress = false
            )
        )
        return DataState(data = Data(Event(userViewState), null))
    }

    fun createUserViewState(): LiveData<UserViewState> {
        val usersLiveData = MutableLiveData<List<User>>()
        val userList = arrayListOf(TEST_USER_1)
        usersLiveData.value = userList
        return getUserViewStateFromUserList(usersLiveData)
    }

    fun getUserViewStateFromUserList(users: LiveData<List<User>>): LiveData<UserViewState> {
        return users.switchMap {
            object : LiveData<UserViewState>() {
                override fun onActive() {
                    super.onActive()
                    value = UserViewState(
                        UserViewState.UserFields(
                            userList = it,
                            isQueryInProgress = true
                        )
                    )
                }
            }
        }
    }


}