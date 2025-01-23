package com.dwan.ta_pam.repository

import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.service.PasienService
import java.io.IOException

// Interface PasienRepository yang mendefinisikan operasi CRUD untuk entitas Pasien
interface PasienRepo {
    suspend fun getPasien(): List<Pasien> // Mendapatkan daftar semua pasien
    suspend fun insertPasien(pasien: Pasien) // Menambahkan pasien baru
    suspend fun updatePasien(id_pasien: String, pasien: Pasien) // Memperbarui data pasien berdasarkan ID
    suspend fun deletePasien(id_pasien: String) // Menghapus pasien berdasarkan ID
    suspend fun getPasienById(id_pasien: String): Pasien // Mendapatkan pasien berdasarkan ID
}

// Implementasi PasienRepo menggunakan jaringan (API)
class NetworkPasienRepo(
    private val pasienApiService: PasienService // Dependensi service untuk API pasien
) : PasienRepo {

    // Implementasi metode untuk menambahkan pasien melalui API
    override suspend fun insertPasien(pasien: Pasien) {
        pasienApiService.insertPasien(pasien)
    }

    // Implementasi metode untuk memperbarui pasien melalui API
    override suspend fun updatePasien(id_pasien: String, pasien: Pasien) {
        pasienApiService.updatePasien(id_pasien, pasien)
    }

    // Implementasi metode untuk menghapus pasien melalui API
    override suspend fun deletePasien(id_pasien: String) {
        try {
            val response = pasienApiService.deletePasien(id_pasien) // Memanggil API untuk menghapus pasien
            if (!response.isSuccessful) { // Jika respons gagal
                throw IOException("Failed to delete Pasien. HTTP Status code: ${response.code()}") // Lempar exception
            } else {
                response.message() // Mendapatkan pesan sukses
                println(response.message()) // Mencetak pesan ke log
            }
        } catch (e: Exception) {
            throw e // Lempar exception jika terjadi error
        }
    }

    // Implementasi metode untuk mendapatkan daftar pasien melalui API
    override suspend fun getPasien(): List<Pasien> = pasienApiService.getPasien()

    // Implementasi metode untuk mendapatkan pasien berdasarkan ID melalui API
    override suspend fun getPasienById(id_pasien: String): Pasien {
        return pasienApiService.getPasienById(id_pasien)
    }
}
