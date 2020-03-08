package com.jizhe7550.celoandroidtest.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.util.Constants.Companion.PAGINATION_PAGE_SIZE


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query(
        """
        SELECT * FROM user
        WHERE title LIKE '%' || :query || '%'
        OR last LIKE '%' || :query || '%'
        OR first LIKE '%' || :query || '%'
        LIMIT (:page * :pageSize)
        """
    )
    fun searchUsers(
        query: String = "",
        page: Int = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<User>>

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Query(
        """
        SELECT * FROM user 
        WHERE gender = :filter AND (title LIKE '%' || :query || '%' 
        OR last LIKE '%' || :query || '%' 
        OR first LIKE '%' || :query || '%') 
        LIMIT (:page * :pageSize)"""
    )
    fun searchUsersByGender(
        query: String = "",
        filter: String = "",
        page: Int = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<User>>
}