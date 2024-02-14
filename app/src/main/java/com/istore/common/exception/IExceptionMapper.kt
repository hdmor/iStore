package com.istore.common.exception

import android.util.Log
import com.istore.R
import com.istore.common.TAG
import org.json.JSONObject
import retrofit2.HttpException

class IExceptionMapper {

    companion object {
        fun map(throwable: Throwable): IException {

            if (throwable is HttpException) {
                // extract message value from server message json
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")
                    return IException(
                        if (throwable.code() == 401) IException.Type.AUTH else IException.Type.SIMPLE,
                        serverMessage = errorMessage
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "map: $e")
                }

            }
            return IException(IException.Type.SIMPLE, R.string.unknown_error)
        }
    }
}