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

    // Fungsi untuk mendapatkan data pasien berdasarkan ID Pasien
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
                    id_pasien = updateUiState.updateUiEvent.id_pasien, // Ambil ID Pasien dari updateUiState
                    pasien = updateUiState.updateUiEvent.toPas() // Konversi event menjadi Pasien
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}