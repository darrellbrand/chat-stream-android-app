package com.djf.chatclient.remote

import com.djf.chatclient.BuildConfig
import com.djf.chatclient.dto.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

private const val apiKeyHeader = BuildConfig.X_API_KEY
private const val basicAuthHeader = BuildConfig.X_BASIC_AUTH

interface ApiService {
    @GET("token")
    @Headers("Authorization: Basic $basicAuthHeader")
    suspend fun initUser(
        @Header(apiKeyHeader) headerValue: String,
        @Query("email") email: String
    ): Response
}