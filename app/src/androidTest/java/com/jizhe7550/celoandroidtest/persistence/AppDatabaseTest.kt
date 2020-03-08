package com.jizhe7550.celoandroidtest.persistence

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before


abstract class AppDatabaseTest {

    // system under test
    private lateinit var appDatabase: AppDatabase

    fun getUserDao(): UserDao {
        return appDatabase.getUserDao()
    }

    @Before
    fun init() {
        appDatabase = inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun finish() {
        appDatabase.close()
    }
}