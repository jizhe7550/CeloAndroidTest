package com.jizhe7550.celoandroidtest.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.jizhe7550.celoandroidtest.api.user.UserApiService
import com.jizhe7550.celoandroidtest.api.user.responses.UserListSearchResponse
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.persistence.UserDao
import com.jizhe7550.celoandroidtest.persistence.returnUserQuery
import com.jizhe7550.celoandroidtest.repository.JobManager
import com.jizhe7550.celoandroidtest.repository.NetworkBoundResource
import com.jizhe7550.celoandroidtest.session.SessionManager
import com.jizhe7550.celoandroidtest.ui.DataState
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState
import com.jizhe7550.celoandroidtest.ui.user.state.UserViewState.*
import com.jizhe7550.celoandroidtest.util.AbsentLiveData
import com.jizhe7550.celoandroidtest.util.ApiSuccessResponse
import com.jizhe7550.celoandroidtest.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.jizhe7550.celoandroidtest.util.DateUtils
import com.jizhe7550.celoandroidtest.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

class UserRepository
@Inject
constructor(
    val userApiService: UserApiService,
    val userDao: UserDao,
    val sessionManager: SessionManager
) : JobManager("UserRepository") {

    private val TAG: String = "AppDebug"

    fun searchUsers(
        query: String,
        filter: String,
        page: Int
    ): LiveData<DataState<UserViewState>> {
        return object : NetworkBoundResource<UserListSearchResponse, List<User>, UserViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main) {

                    // finishing by viewing db cache
                    val dbSource = loadFromCache()
                    result.addSource(dbSource) { viewState ->
                        viewState.userFields.isQueryInProgress = false
                        if (page * PAGINATION_PAGE_SIZE > viewState.userFields.userList.size) {
                            viewState.userFields.isQueryExhausted = true
                        }
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<UserListSearchResponse>
            ) {

                val userList: ArrayList<User> = ArrayList()
                for (userResponse in response.body.results) {
                    userList.add(
                        User(
                            userResponse.email ?: "",
                            userResponse.phone ?: "",
                            userResponse.cell ?: "",
                            userResponse.name.title ?: "",
                            userResponse.picture.thumbnail ?: "",
                            userResponse.picture.medium ?: "",
                            userResponse.picture.large ?: "",
                            userResponse.gender ?: "",
                            userResponse.name.first ?: "",
                            userResponse.name.last ?: "",
                            userResponse.dob.age ?: 0,
                            DateUtils.convertServerStringDateToLong(userResponse.dob.date?:"")
                        )
                    )
                }

                updateLocalDb(userList)

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<UserListSearchResponse>> {
                return userApiService.searchListUsers(
                    results = PAGINATION_PAGE_SIZE,
                    gender = filter,
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<UserViewState> {
                return userDao.returnUserQuery(
                    query = query,
                    filter = filter,
                    page = page
                )
                    .switchMap {
                        object : LiveData<UserViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = UserViewState(
                                    UserFields(
                                        userList = it,
                                        isQueryInProgress = true
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<User>?) {
                // loop through list and update the local db
                if (cacheObject != null) {
                    withContext(Dispatchers.IO) {
                        for (user in cacheObject) {
                            try {
                                // Launch each insert as a separate job to be executed in parallel
                                launch {
                                    Log.d(TAG, "updateLocalDb: inserting user: $user")
                                    userDao.insert(user)
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "updateLocalDb: error updating cache data on user with email: ${user.email}. " +
                                            "${e.message}"
                                )
                                // Could send an error report here or something but I don't think you should throw an error to the UI
                                // Since there could be many blog posts being inserted/updated.
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "updateLocalDb: user list is null")
                }
            }

            override fun setJob(job: Job) {
                addJob("searchUsers", job)
            }

        }.asLiveData()
    }

    fun restoreUserListFromCache(
        query: String,
        filter: String,
        page: Int
    ): LiveData<DataState<UserViewState>> {
        return object : NetworkBoundResource<UserListSearchResponse, List<User>, UserViewState>(
            sessionManager.isConnectedToTheInternet(),
            false,
            false,
            true
        ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        viewState.userFields.isQueryInProgress = false
                        if (page * PAGINATION_PAGE_SIZE > viewState.userFields.userList.size) {
                            viewState.userFields.isQueryExhausted = true
                        }
                        onCompleteJob(
                            DataState.data(
                                viewState,
                                null
                            )
                        )
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<UserListSearchResponse>
            ) {
                // ignore
            }

            override fun createCall(): LiveData<GenericApiResponse<UserListSearchResponse>> {
                return AbsentLiveData.create()
            }

            override fun loadFromCache(): LiveData<UserViewState> {
                return userDao.returnUserQuery(
                    query = query,
                    filter = filter,
                    page = page
                )
                    .switchMap {
                        object : LiveData<UserViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = UserViewState(
                                    UserFields(
                                        userList = it,
                                        isQueryInProgress = true
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<User>?) {
                // ignore
            }

            override fun setJob(job: Job) {
                addJob("restoreBlogListFromCache", job)
            }

        }.asLiveData()
    }

}







