package com.uth.week05.uthsmarttasks.network

import com.uth.week05.uthsmarttasks.model.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("product")
    suspend fun getProductDetail(): ProductResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://mock.apidog.com/m1/890655-872447-default/v2/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}