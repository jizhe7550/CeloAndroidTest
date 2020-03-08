package com.jizhe7550.celoandroidtest.api.user.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserListSearchResponse(
    @SerializedName("results")
    @Expose
    var results: List<UserSearchResponse>,

    @SerializedName("error")
    @Expose
    var error: String
) {
    override fun toString(): String {
        return "UserListResponse(results=$results, error=$error)"
    }
}