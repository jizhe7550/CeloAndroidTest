package com.jizhe7550.celoandroidtest.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jizhe7550.celoandroidtest.util.TestUtil
import com.jizhe7550.celoandroidtest.model.User
import com.jizhe7550.celoandroidtest.util.LiveDataTestUtil
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test


class UserDaoTest : AppDatabaseTest() {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    /*
     * Insert, Read
     */
    @Test
    @Throws(Exception::class)
    fun insertReadAll() = runBlocking {
        val user1 = TestUtil.TEST_USER_1
        val user2 = TestUtil.TEST_USER_2
        // insert
        getUserDao().insert(user1)
        getUserDao().insert(user2)
        // read
        val liveDataTestUtil = LiveDataTestUtil<List<User>>()
        val insertedUsers = liveDataTestUtil.getValue(getUserDao().getAllUsers())

        assertNotNull(insertedUsers)

        assertEquals(2, insertedUsers?.size)
    }

    /*
    * Insert, Read,filter male/female
    */
    @Test
    @Throws(Exception::class)
    fun insertReadFilter() = runBlocking {
        val user1 = TestUtil.TEST_USER_1//male
        val user2 = TestUtil.TEST_USER_2//female
        // insert
        getUserDao().insert(user1)
        getUserDao().insert(user2)
        // read
        val liveDataTestUtil = LiveDataTestUtil<List<User>>()
        val insertedUsers = liveDataTestUtil.getValue(getUserDao().getAllUsers())

        assertNotNull(insertedUsers)
        assertEquals(2, insertedUsers!!.size)

        //test male
        val maleUser = liveDataTestUtil.getValue(getUserDao().searchUsersByGender(filter = "male"))
        assertNotNull(maleUser)
        assertEquals(1, maleUser!!.size)
        assertEquals("male", maleUser[0].gender)

        //test female
        val femaleUser =
            liveDataTestUtil.getValue(getUserDao().searchUsersByGender(filter = "female"))
        assertNotNull(femaleUser)
        assertEquals(1, femaleUser!!.size)
        assertEquals("female", femaleUser[0].gender)
    }

    /*
    * Insert, Read by keywords
    */
    @Test
    @Throws(Exception::class)
    fun insertReadByKeyWord() = runBlocking {
        val user1 = TestUtil.TEST_USER_1//male   name: aa1 bbb1
        val user2 = TestUtil.TEST_USER_2//female name aa2 bbb2
        val user3 = TestUtil.TEST_USER_3//female name cc1 ddd1
        val user4 = TestUtil.TEST_USER_4//male  name cc2 ddd2
        // insert
        getUserDao().insert(user1)
        getUserDao().insert(user2)
        getUserDao().insert(user3)
        getUserDao().insert(user4)
        // read
        val liveDataTestUtil = LiveDataTestUtil<List<User>>()
        val insertedUsers = liveDataTestUtil.getValue(getUserDao().getAllUsers())

        assertNotNull(insertedUsers)
        assertEquals(4, insertedUsers!!.size)

        //test query by name with "aa" ,should be user1 user2
        val aaUsers = liveDataTestUtil.getValue(getUserDao().searchUsers(query = "aa"))
        assertEquals(2, aaUsers!!.size)
        assertEquals(user1, aaUsers[0])
        assertEquals(user2, aaUsers[1])
    }

    /*
    * Insert, Read filter male/female and keywords
    */
    @Test
    @Throws(Exception::class)
    fun insertReadByGenderAndKeyWord() = runBlocking {
        val user1 = TestUtil.TEST_USER_1//male   name: aa1 bbb1
        val user2 = TestUtil.TEST_USER_2//female name aa2 bbb2
        val user3 = TestUtil.TEST_USER_3//female name cc1 ddd1
        val user4 = TestUtil.TEST_USER_4//male  name cc2 ddd2
        // insert
        getUserDao().insert(user1)
        getUserDao().insert(user2)
        getUserDao().insert(user3)
        getUserDao().insert(user4)
        // read
        val liveDataTestUtil = LiveDataTestUtil<List<User>>()
        val insertedUsers = liveDataTestUtil.getValue(getUserDao().getAllUsers())

        assertNotNull(insertedUsers)
        assertEquals(4, insertedUsers!!.size)

        //test query by name with "cc" and fitler female ,should be user3
        val aaUsers = liveDataTestUtil.getValue(getUserDao().searchUsersByGender(query = "cc", filter = "female"))
        assertEquals(1, aaUsers!!.size)
        assertEquals(user3, aaUsers[0])
    }
}