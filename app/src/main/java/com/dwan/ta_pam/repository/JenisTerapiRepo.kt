package com.dwan.ta_pam.repository

import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.service.JenisTerapiService
import java.io.IOException

interface JenisTerapiRepo {
    suspend fun getJenis(): List<JenisTerapi>
    suspend fun insertJenis(jenisTerapi: JenisTerapi)
    suspend fun updateJenis(id_jenis_terapi: String, jenisTerapi: JenisTerapi)
    suspend fun deleteJenis(id_jenis_terapi: String)
    suspend fun getJenisById(id_jenis_terapi: String): JenisTerapi
}

class NetworkJenisTerapiRepo(
    private val jenisTerapiApiService: JenisTerapiService
) : JenisTerapiRepo {
    override suspend fun insertJenis(jenisTerapi: JenisTerapi) {
        jenisTerapiApiService.insertJenis(jenisTerapi)
    }

    override suspend fun updateJenis(id_jenis_terapi: String, jenisTerapi: JenisTerapi) {
        jenisTerapiApiService.updateJenis(id_jenis_terapi, jenisTerapi)
    }

    override suspend fun deleteJenis(id_jenis_terapi: String) {
        try {
            val response = jenisTerapiApiService.deleteJenis(id_jenis_terapi)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete Jenis Terapi. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getJenis(): List<JenisTerapi> = jenisTerapiApiService.getJenis()
    override suspend fun getJenisById(id_jenis_terapi: String): JenisTerapi {
        return jenisTerapiApiService.getJenisById(id_jenis_terapi)
    }
}