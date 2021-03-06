package com.jizhe7550.celoandroidtest.util

class Constants {

    companion object{

        const val BASE_URL = "https://randomuser.me/api/"

        const val NETWORK_TIMEOUT = 6000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val PAGINATION_PAGE_SIZE = 10
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
    }
}