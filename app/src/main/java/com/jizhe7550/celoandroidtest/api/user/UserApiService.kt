package com.jizhe7550.celoandroidtest.api.user

import androidx.lifecycle.LiveData
import com.jizhe7550.celoandroidtest.api.user.responses.UserListSearchResponse
import com.jizhe7550.celoandroidtest.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface UserApiService {

    @GET(".")
    fun searchListUsers(
        @Query("results") results: Int, // num of every page
        @Query("gender") gender: String,
        @Query("page") page: Int // start from 1
    ): LiveData<GenericApiResponse<UserListSearchResponse>>
}