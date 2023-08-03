package com.muhammhassan.epatrol.core.utils

import okio.IOException
import org.json.JSONObject
import retrofit2.Response

object Utils {
    fun <T> Response<T>.parseError(key: String = "message"): String{
        return try{
            this.errorBody()?.string().runCatching {
                this?.let{
                    JSONObject(it).getString(key)
                }
            }.getOrNull() ?: this.message()
        }catch (e: IOException){
            e.message.toString()
        }
    }
}