package com.jizhe7550.celoandroidtest.api.user.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserSearchResponse(
    @SerializedName("dob")
    @Expose
    val dob: Dob,
    @SerializedName("gender")
    @Expose
    val gender: String?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("name")
    @Expose
    val name: Name,
    @SerializedName("picture")
    @Expose
    val picture: Picture,
    @SerializedName("phone")
    @Expose
    val phone: String?,
    @SerializedName("cell")
    @Expose
    val cell: String?
) {

    data class Picture(
        @SerializedName("large")
        @Expose
        val large: String?,
        @SerializedName("medium")
        @Expose
        val medium: String?,
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String?
    )

    data class Name(
        @SerializedName("first")
        @Expose
        val first: String?,
        @SerializedName("last")
        @Expose
        val last: String?,
        @SerializedName("title")
        @Expose
        val title: String?
    )

    data class Dob(
        @SerializedName("age")
        @Expose
        val age: Int?,
        @SerializedName("date")
        @Expose
        val date: String?
    )
}


