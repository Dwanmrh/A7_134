package com.dwan.ta_pam.service

import com.dwan.ta_pam.model.JenisTerapi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface JenisTerapiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacajenis.php")
    suspend fun getJenis(): List<JenisTerapi>

    @GET("baca1jenis.php/{id_jenis_terapi}")
    suspend fun getJenisById(@Query("id_jenis_terapi")id_jenis_terapi:String): JenisTerapi

    @POST("insertjenis.php")
    suspend fun insertJenis(@Body jenisTerapi: JenisTerapi)

    @PUT("editjenis.php/{id_jenis_terapi}")
    suspend fun updateJenis(@Query("id_jenis_terapi")id_jenis_terapi: String, @Body jenisTerapi: JenisTerapi)

    @DELETE("deletejenis.php/{id_jenis_terapi}")
    suspend fun deleteJenis(@Query("id_jenis_terapi")id_jenis_terapi: String): Response<Void>
}