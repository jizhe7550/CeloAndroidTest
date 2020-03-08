package com.jizhe7550.celoandroidtest.util

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class ErrorHandling{

    companion object{

        private val TAG: String = "AppDebug"

        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"

        const val PAGINATION_DONE_ERROR = "Invalid page."
        const val ERROR_CHECK_API = "Uh oh, something has gone wrong. Please tweet us @randomapi about the issue. Thank you."
        const val ERROR_UNKNOWN = "Unknown error"


        fun isNetworkError(msg: String): Boolean{
            return when{
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
                else-> false
            }
        }

        private fun parseDetailJsonResponse(rawJson: String?): String{
            Log.d(TAG, "parseDetailJsonResponse: $rawJson")
            try{
                if(!rawJson.isNullOrBlank()){
                    if(rawJson == ERROR_CHECK_API){
                        return PAGINATION_DONE_ERROR
                    }
                    return JSONObject(rawJson).get("error") as String
                }
            }catch (e: JSONException){
                Log.e(TAG, "parseDetailJsonResponse: ${e.message}")
            }
            return ""
        }

        fun isPaginationDone(errorResponse: String?): Boolean{
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return PAGINATION_DONE_ERROR == parseDetailJsonResponse(errorResponse)
        }
    }

}