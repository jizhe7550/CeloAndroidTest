package com.jizhe7550.celoandroidtest.ui

/**
 * encapsulate usual ui interaction ways
 */
data class UIMessage(
    val message: String,
    val uiMessageType: UIMessageType
)

sealed class UIMessageType{

    object Toast : UIMessageType()

    object Dialog : UIMessageType()

    class AreYouSureDialog(
        val callback: AreYouSureCallback
    ): UIMessageType()

    object None : UIMessageType()
}