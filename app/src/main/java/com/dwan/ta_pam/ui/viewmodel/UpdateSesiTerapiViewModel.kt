package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.repository.SesiTerapiRepo
import kotlinx.coroutines.launch

class UpdateSesiTerapiViewModel(private val sesi: SesiTerapiRepo) : ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var updateSUiState by mutableStateOf(UpdateSUiState())
        private set

    // Fungsi untuk mendapatkan data sesi terapi berdasarkan NIM
    fun getSesiTerapiById(id_sesi: String) {
        viewModelScope.launch {
            try {
                val sesiTerapi = sesi.getSesiById(id_sesi)
                updateSUiState = updateSUiState.copy(
                    updateSUiEvent = UpdateSUiEvent(
                        id_sesi = sesiTerapi.id_sesi,
                        tanggal_sesi = sesiTerapi.tanggal_sesi,
                        id_pasien = sesiTerapi.id_pasien,
                        id_terapis = sesiTerapi.id_terapis,
                        id_jenis_terapi = sesiTerapi.id_jenis_terapi,
                        catatan_sesi = sesiTerapi.catatan_sesi
                    )
                )
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateSesiTerapiState(updateSUiEvent: UpdateSUiEvent) {
        updateSUiState = UpdateSUiState(updateSUiEvent = updateSUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk memuat data sesi terapi ke dalam form untuk di-update
    fun loadSesiTerapiData(sesiTerapi: SesiTerapi) {
        updateSUiState = sesiTerapi.toUpdateUiStateSesiTerapi()
    }

    // Fungsi untuk memperbarui data sesi terapi ke database
    fun updateSesiTerapi() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                sesi.updateSesi(
                    id_sesi = updateSUiState.updateSUiEvent.id_sesi, // Ambil NIM dari updateUiState
                    sesiTerapi = updateSUiState.updateSUiEvent.toSesi() // Konversi event menjadi sesi terapi
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form update terapis
data class UpdateSUiState(
    val updateSUiEvent: UpdateSUiEvent = UpdateSUiEvent() // State default berisi objek kosong dari UpdateUiEvent
)

// Menyimpan data input pengguna untuk form update sesi terapi
data class UpdateSUiEvent(
    val id_sesi: String = "",
    val tanggal_sesi: String = "",
    val id_pasien: String = "",
    val id_terapis: String = "",
    val id_jenis_terapi: String = "",
    val catatan_sesi: String = ""
)

// Fungsi untuk mengubah data UpdateUiEvent menjadi sesi terapi
fun UpdateSUiEvent.toSesi(): SesiTerapi = SesiTerapi( // UpdateUiEvent > sesi terapi > Simpan data Pas ke db
    id_sesi = id_sesi, // Memindahkan nilai NIM dari UpdateUiEvent ke sesi terapi
    tanggal_sesi = tanggal_sesi,
    id_pasien = id_pasien,
    id_terapis = id_terapis,
    id_jenis_terapi = id_jenis_terapi,
    catatan_sesi = catatan_sesi
)

// Fungsi untuk mengubah data sesi terapi menjadi UpdateUiState
fun SesiTerapi.toUpdateUiStateSesiTerapi(): UpdateSUiState = UpdateSUiState( // sesi terapi > updateUiEvent > Masuk ke UpdateUiState
    updateSUiEvent = toUpdateSUiEvent() // Memanggil fungsi toUpdateUiEvent untuk mengonversi data sesi terapi
)

// Fungsi untuk mengubah data sesi terapi menjadi data UpdateUiEvent
fun SesiTerapi.toUpdateSUiEvent(): UpdateSUiEvent = UpdateSUiEvent(
    id_sesi = id_sesi, // Memindahkan nilai NIM dari sesi terapi ke UpdateUiEvent
    tanggal_sesi = tanggal_sesi,
    id_pasien = id_pasien,
    id_terapis = id_terapis,
    id_jenis_terapi = id_jenis_terapi,
    catatan_sesi = catatan_sesi
)