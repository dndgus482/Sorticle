package com.example.android.helper

import com.example.android.interfaces.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHelper {
    val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl("https://7c15ad8f-1398-4335-ae9c-bac5ab7403fe.mock.pstmn.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    val apiService = retrofit.create(APIService::class.java)
}