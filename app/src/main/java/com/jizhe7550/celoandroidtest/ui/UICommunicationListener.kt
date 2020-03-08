package com.jizhe7550.celoandroidtest.ui

/**
 * using for Fragment feedbacks Activity to handle UI eg toast dialog....
 */
interface UICommunicationListener {

    fun onUIMessageReceived(uiMessage: UIMessage)
}