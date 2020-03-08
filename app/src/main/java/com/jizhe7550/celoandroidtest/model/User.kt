package com.jizhe7550.celoandroidtest.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone:String,

    @ColumnInfo(name = "cell")
    val cell:String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,

    @ColumnInfo(name = "medium")
    val medium: String,

    @ColumnInfo(name = "large")
    val large: String,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "first")
    val first: String,

    @ColumnInfo(name = "last")
    val last: String,

    @ColumnInfo(name = "age")
    val age: Int,

    @ColumnInfo(name = "date")
    val date: Long

) : Parcelable