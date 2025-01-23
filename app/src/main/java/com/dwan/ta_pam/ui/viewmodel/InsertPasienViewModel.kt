package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.repository.PasienRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form tambah pasien
class InsertPasienViewModel(private val pas: PasienRepo): ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var insertUiState by mutableStateOf(InsertUiState())
        private set

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateInsertPasState(insertUiEvent: InsertUiEvent) {
        insertUiState = InsertUiState(insertUiEvent = insertUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk menambahkan data pasien ke database
    suspend fun insertPas() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                pas.insertPasien(insertUiState.insertUiEvent.toPas())
            }catch (e:Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form input pasien
data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent() // State default berisi objek kosong dari InsertUiEvent
)

// Menyimpan data input pengguna untuk form pasien
data class InsertUiEvent(
    val id_pasien: String = "",
    val nama_pasien: String = "",
    val alamat: String = "",
    val nomor_telepon: String = "",
    val tanggal_lahir: String = "",
    val riwayat_medikal: String = ""
)
