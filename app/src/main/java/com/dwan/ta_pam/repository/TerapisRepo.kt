package com.dwan.ta_pam.repository

import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.service.TerapisService
import java.io.IOException

interface TerapisRepo {
    suspend fun getTerapis(): List<Terapis>
    suspend fun insertTerapis(terapis: Terapis)
    suspend fun updateTerapis(id_terapis: String, terapis: Terapis)
    suspend fun deleteTerapis(id_terapis: String)
    suspend fun getTerapisById(id_terapis: String): Terapis
}

class NetworkTerapisRepo(
    private val terapisApiService: TerapisService
) : TerapisRepo {
    override suspend fun insertTerapis(terapis: Terapis) {
        terapisApiService.insertTerapis(terapis)
    }

    override suspend fun updateTerapis(id_terapis: String, terapis: Terapis) {
        terapisApiService.updateTerapis(id_terapis, terapis)
    }

    override suspend fun deleteTerapis(id_terapis: String) {
        try {
            val response = terapisApiService.deleteTerapis(id_terapis)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete Terapis. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getTerapis(): List<Terapis> = terapisApiService.getTerapis()
    override suspend fun getTerapisById(id_terapis: String): Terapis {
        return terapisApiService.getTerapisById(id_terapis)
    }
}