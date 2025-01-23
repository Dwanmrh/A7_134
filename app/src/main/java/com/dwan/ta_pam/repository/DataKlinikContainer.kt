package com.dwan.ta_pam.repository

import com.dwan.ta_pam.service.PasienService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val pasienRepo: PasienRepo

}

class DataKlinikContainer : AppContainer {
    private val baseurl = "http://10.0.2.2:8081/screening/" // localhost diganti ip kalo run di hp
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseurl).build()

    private val pasienService: PasienService by lazy {
        retrofit.create(PasienService::class.java)
    }


    override val pasienRepo: PasienRepo by lazy {
        NetworkPasienRepo(pasienService)
    }
}