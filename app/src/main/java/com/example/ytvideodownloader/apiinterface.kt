package com.example.ytvideodownloader

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class Videorequest( val url :String)

interface apiinterface {
    @POST("/download")
    fun downloadVideo(@Body request: Videorequest): Call<ResponseBody>
}
