package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.repository.TerapisRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form tambah mahasiswa
class InsertTerapisViewModel(private val trps: TerapisRepo): ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var insertTUiState by mutableStateOf(InsertTUiState())
        private set

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateInsertTerState(insertTerUiEvent: InsertTUiEvent) {
        insertTUiState = InsertTUiState(insertTUiEvent = insertTerUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk menambahkan data mahasiswa ke database
    suspend fun insertTer() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                trps.insertTerapis(insertTUiState.insertTUiEvent.toTrps())
            }catch (e:Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form input terapis
data class InsertTUiState(
    val insertTUiEvent: InsertTUiEvent = InsertTUiEvent() // State default berisi objek kosong dari InsertUiEvent
)

// Menyimpan data input pengguna untuk form terapis
data class InsertTUiEvent(
    val id_terapis: String = "",
    val nama_terapis: String = "",
    val spesialisasi: String = "",
    val nomor_izin_praktik: String = ""
)

// Fungsi untuk mengubah data InsertUiEvent menjadi terapis
fun InsertTUiEvent.toTrps(): Terapis = Terapis( // InsertUiEvent > Mahasiswa > Simpan data Mhs ke db
    id_terapis = id_terapis, // Memindahkan nilai NIM dari InsertUiEvent ke Mahasiswa
    nama_terapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomor_izin_praktik = nomor_izin_praktik
)

// Fungsi untuk mengubah data Mahasiswa menjadi InsertUiState
fun Terapis.toInsertTUiStateTer(): InsertTUiState = InsertTUiState( // Mahasiswa > insertUiEvent > Masuk ke InsertUiState
    insertTUiEvent = toInsertTUiEvent() // Memanggil fungsi toInsertUiEvent untuk mengonversi data Mahasiswa
)

// Fungsi untuk mengubah data Mahasiswa menjadi data InsertUiEvent
fun Terapis.toInsertTUiEvent(): InsertTUiEvent = InsertTUiEvent(
    id_terapis = id_terapis, // Memindahkan nilai NIM dari Mahasiswa ke InsertUiEvent
    nama_terapis = nama_terapis,
    spesialisasi = spesialisasi,
    nomor_izin_praktik = nomor_izin_praktik
)