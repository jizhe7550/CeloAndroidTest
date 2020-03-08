package com.jizhe7550.celoandroidtest.ui

/**
 * when dateState change handle UI
 */
interface DataStateChangeListener{

    fun onDataStateChange(dataState: DataState<*>?)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean
}