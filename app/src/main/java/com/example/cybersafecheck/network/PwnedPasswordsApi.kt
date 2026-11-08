package com.example.cybersafecheck.network

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Retrofit turns this interface into a real web request.
// Only the first 5 characters of the password's hash are ever sent.
interface PwnedPasswordsApi {

    @GET("range/{prefix}")
    suspend fun checkRange(@Path("prefix") prefix: String): ResponseBody
}

// Builds the Retrofit object once and shares it
object PwnedPasswordsClient {

    val api: PwnedPasswordsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pwnedpasswords.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        retrofit.create(PwnedPasswordsApi::class.java)
    }
}

//Author: Mangesana E
//Student number: 2030630053
