package com.dwan.ta_pam.service

import com.dwan.ta_pam.model.Terapis
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TerapisService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacaterapis.php")
    suspend fun getTerapis(): List<Terapis>

    @GET("baca1terapis.php/{id_terapis}")
    suspend fun getTerapisById(@Query("id_terapis")id_terapis:String): Terapis

    @POST("insertterapis.php")
    suspend fun insertTerapis(@Body terapis: Terapis)

    @PUT("editterapis.php/{id_terapis}")
    suspend fun updateTerapis(@Query("id_terapis")id_terapis: String, @Body terapis: Terapis)

    @DELETE("deleteterapis.php/{id_terapis}")
    suspend fun deleteTerapis(@Query("id_terapis")id_terapis: String): Response<Void>
}