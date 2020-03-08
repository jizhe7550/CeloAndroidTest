package com.jizhe7550.celoandroidtest.persistence


import androidx.room.Database
import androidx.room.RoomDatabase
import com.jizhe7550.celoandroidtest.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }
}