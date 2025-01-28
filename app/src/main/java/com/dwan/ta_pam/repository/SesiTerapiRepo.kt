package com.dwan.ta_pam.repository

import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.service.SesiTerapiService
import java.io.IOException

interface SesiTerapiRepo {
    suspend fun getSesi(): List<SesiTerapi>
    suspend fun insertSesi(sesiTerapi: SesiTerapi)
    suspend fun updateSesi(id_sesi: String, sesiTerapi: SesiTerapi)
    suspend fun deleteSesi(id_sesi: String)
    suspend fun getSesiById(id_sesi: String): SesiTerapi
}

class NetworkSesiTerapiRepo(
    private val sesiTerapiApiService: SesiTerapiService
) : SesiTerapiRepo {
    override suspend fun insertSesi(sesiTerapi: SesiTerapi) {
        sesiTerapiApiService.insertSesi(sesiTerapi)
    }

    override suspend fun updateSesi(id_sesi: String, sesiTerapi: SesiTerapi) {
        sesiTerapiApiService.updateSesi(id_sesi, sesiTerapi)
    }

    override suspend fun deleteSesi(id_sesi: String) {
        try {
            val response = sesiTerapiApiService.deleteSesi(id_sesi)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete Sesi Terapi. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getSesi(): List<SesiTerapi> = sesiTerapiApiService.getSesi()
    override suspend fun getSesiById(id_sesi: String): SesiTerapi {
        return sesiTerapiApiService.getSesiById(id_sesi)
    }
}