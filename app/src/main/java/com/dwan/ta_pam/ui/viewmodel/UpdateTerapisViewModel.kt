package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.repository.TerapisRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form update terapis
class UpdateTerapisViewModel(private val trps: TerapisRepo) : ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var updateTUiState by mutableStateOf(UpdateTUiState())
        private set

    // Fungsi untuk mendapatkan data terapis berdasarkan ID
    fun getTerapisById(id_terapis: String) {
        viewModelScope.launch {
            try {
                val terapis = trps.getTerapisById(id_terapis)
                updateTUiState = updateTUiState.copy(
                    updateTUiEvent = UpdateTUiEvent(
                        id_terapis = terapis.id_terapis,
                        nama_terapis = terapis.nama_terapis,
                        spesialisasi = terapis.spesialisasi,
                        nomor_izin_praktik = terapis.nomor_izin_praktik
                    )
                )
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateTerapisState(updateTUiEvent: UpdateTUiEvent) {
        updateTUiState = UpdateTUiState(updateTUiEvent = updateTUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk memuat data terapis ke dalam form untuk di-update
    fun loadTerapisData(terapis: Terapis) {
        updateTUiState = terapis.toUpdateUiStateTerapis()
    }

    // Fungsi untuk memperbarui data terapis ke database
    fun updateTerapis() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                trps.updateTerapis(
                    id_terapis = updateTUiState.updateTUiEvent.id_terapis, // Ambil ID dari updateUiState
                    terapis = updateTUiState.updateTUiEvent.toTrps() // Konversi event menjadi terapis
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form update terapis
data class UpdateTUiState(
    val updateTUiEvent: UpdateTUiEvent = UpdateTUiEvent() // State default berisi objek kosong dari UpdateUiEvent
)

// Menyimpan data input pengguna untuk form update terapis
data class UpdateTUiEvent(
    val id_terapis: String = "",
    val nama_terapis: String = "",
    val spesialisasi: String = "",
    val nomor_izin_praktik: String = ""
)

// Fungsi untuk mengubah data UpdateUiEvent menjadi terapis
fun UpdateTUiEvent.toTrps(): Terapis = Terapis( // UpdateUiEvent > terapis > Simpan data Pas ke db
    id_terapis = id_terapis, // Memindahkan nilai ID dari UpdateUiEvent ke terapis
    nama_terapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomor_izin_praktik = nomor_izin_praktik
)

// Fungsi untuk mengubah data terapis menjadi UpdateUiState
fun Terapis.toUpdateUiStateTerapis(): UpdateTUiState = UpdateTUiState( // terapis > updateUiEvent > Masuk ke UpdateUiState
    updateTUiEvent = toUpdateTUiEvent() // Memanggil fungsi toUpdateUiEvent untuk mengonversi data terapis
)

// Fungsi untuk mengubah data terapis menjadi data UpdateUiEvent
fun Terapis.toUpdateTUiEvent(): UpdateTUiEvent = UpdateTUiEvent(
    id_terapis = id_terapis, // Memindahkan nilai ID dari terapis ke UpdateUiEvent
    nama_terapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomor_izin_praktik = nomor_izin_praktik
)