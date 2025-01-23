package com.dwan.ta_pam.service

import com.dwan.ta_pam.model.Pasien
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PasienService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacapasien.php")
    suspend fun getPasien(): List<Pasien>

    @GET("baca1pasien.php/{id_pasien}")
    suspend fun getPasienById(@Query("id_pasien")id_pasien:String):Pasien

    @POST("insertpasien.php")
    suspend fun insertPasien(@Body pasien: Pasien)

    @PUT("editpasien.php/{id_pasien}")
    suspend fun updatePasien(@Query("id_pasien")id_pasien: String, @Body pasien: Pasien)

    @DELETE("deletepasien.php/{id_pasien}")
    suspend fun deletePasien(@Query("id_pasien")id_pasien: String): Response<Void>
}