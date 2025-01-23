package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Pasien
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

// Fungsi untuk mengubah data InsertUiEvent menjadi pasien
fun InsertUiEvent.toPas(): Pasien = Pasien( // InsertUiEvent > Pasien > Simpan data Pas ke db
    id_pasien = id_pasien, // Memindahkan nilai ID Pasien dari InsertUiEvent ke Pasien
    nama_pasien = nama_pasien,
    alamat = alamat,
    nomor_telepon = nomor_telepon,
    tanggal_lahir = tanggal_lahir,
    riwayat_medikal = riwayat_medikal
)

// Fungsi untuk mengubah data Pasien menjadi InsertUiState
fun Pasien.toInsertUiStatePas(): InsertUiState = InsertUiState( // Pasien > insertUiEvent > Masuk ke InsertUiState
    insertUiEvent = toInsertUiEvent() // Memanggil fungsi toInsertUiEvent untuk mengonversi data Pasien
)

// Fungsi untuk mengubah data Pasien menjadi data InsertUiEvent
fun Pasien.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    id_pasien = id_pasien, // Memindahkan nilai ID Pasien dari Pasien ke InsertUiEvent
    nama_pasien = nama_pasien,
    alamat = alamat,
    nomor_telepon = nomor_telepon,
    tanggal_lahir = tanggal_lahir,
    riwayat_medikal = riwayat_medikal
)