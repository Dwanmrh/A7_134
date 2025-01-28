package com.dwan.ta_pam.service

import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.model.SesiTerapi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SesiTerapiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacasesi.php")
    suspend fun getSesi(): List<SesiTerapi>

    @GET("baca1sesi.php/{id_sesi}")
    suspend fun getSesiById(@Query("id_sesi")id_sesi:String): SesiTerapi

    @POST("insertsesi.php")
    suspend fun insertSesi(@Body sesiTerapi: SesiTerapi)

    @PUT("editsesi.php/{id_sesi}")
    suspend fun updateSesi(@Query("id_sesi")id_sesi: String, @Body sesiTerapi: SesiTerapi)

    @DELETE("deletesesi.php/{id_sesi}")
    suspend fun deleteSesi(@Query("id_sesi")id_sesi: String): Response<Void>
}