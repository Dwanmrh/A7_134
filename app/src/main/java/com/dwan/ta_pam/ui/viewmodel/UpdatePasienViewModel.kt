package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.repository.PasienRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form update pasien
class UpdatePasienViewModel(private val pas: PasienRepo) : ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var updateUiState by mutableStateOf(UpdateUiState())
        private set

    // Fungsi untuk mendapatkan data pasien berdasarkan ID
    fun getPasienById(id_pasien: String) {
        viewModelScope.launch {
            try {
                val pasien = pas.getPasienById(id_pasien)
                updateUiState = updateUiState.copy(
                    updateUiEvent = UpdateUiEvent(
                        id_pasien = pasien.id_pasien,
                        nama_pasien = pasien.nama_pasien,
                        alamat = pasien.alamat,
                        nomor_telepon = pasien.nomor_telepon,
                        tanggal_lahir = pasien.tanggal_lahir,
                        riwayat_medikal = pasien.riwayat_medikal
                    )
                )
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updatePasState(updateUiEvent: UpdateUiEvent) {
        updateUiState = UpdateUiState(updateUiEvent = updateUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk memuat data pasien ke dalam form untuk di-update
    fun loadPasienData(pasien: Pasien) {
        updateUiState = pasien.toUpdateUiStatePas()
    }

    // Fungsi untuk memperbarui data pasien ke database
    fun updatePas() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                pas.updatePasien(
                    id_pasien = updateUiState.updateUiEvent.id_pasien, // Ambil ID dari updateUiState
                    pasien = updateUiState.updateUiEvent.toPas() // Konversi event menjadi pasien
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form update pasien
data class UpdateUiState(
    val updateUiEvent: UpdateUiEvent = UpdateUiEvent() // State default berisi objek kosong dari UpdateUiEvent
)

// Menyimpan data input pengguna untuk form update pasien
data class UpdateUiEvent(
    val id_pasien: String = "",
    val nama_pasien: String = "",
    val alamat: String = "",
    val nomor_telepon: String = "",
    val tanggal_lahir: String = "",
    val riwayat_medikal: String = ""
)

// Fungsi untuk mengubah data UpdateUiEvent menjadi pasien
fun UpdateUiEvent.toPas(): Pasien = Pasien( // UpdateUiEvent > pasien > Simpan data Pas ke db
    id_pasien = id_pasien, // Memindahkan nilai ID dari UpdateUiEvent ke pasien
    nama_pasien = nama_pasien,
    alamat = alamat,
    nomor_telepon = nomor_telepon,
    tanggal_lahir = tanggal_lahir,
    riwayat_medikal = riwayat_medikal
)

// Fungsi untuk mengubah data pasien menjadi UpdateUiState
fun Pasien.toUpdateUiStatePas(): UpdateUiState = UpdateUiState( // pasien > updateUiEvent > Masuk ke UpdateUiState
    updateUiEvent = toUpdateUiEvent() // Memanggil fungsi toUpdateUiEvent untuk mengonversi data pasien
)

// Fungsi untuk mengubah data pasien menjadi data UpdateUiEvent
fun Pasien.toUpdateUiEvent(): UpdateUiEvent = UpdateUiEvent(
    id_pasien = id_pasien, // Memindahkan nilai ID dari pasien ke UpdateUiEvent
    nama_pasien = nama_pasien,
    alamat = alamat,
    nomor_telepon = nomor_telepon,
    tanggal_lahir = tanggal_lahir,
    riwayat_medikal = riwayat_medikal
)