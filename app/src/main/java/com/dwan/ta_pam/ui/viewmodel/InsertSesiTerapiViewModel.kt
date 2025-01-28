package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.repository.SesiTerapiRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form tambah sesi terapi
class InsertSesiTerapiViewModel(private val sesi: SesiTerapiRepo): ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var insertSUiState by mutableStateOf(InsertSUiState())
        private set

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateInsertSesiState(insertSesiUiEvent: InsertSUiEvent) {
        insertSUiState = InsertSUiState(insertSUiEvent = insertSesiUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk menambahkan data sesi terapi ke database
    suspend fun insertSesi() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                sesi.insertSesi(insertSUiState.insertSUiEvent.toSesi())
            }catch (e:Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form input terapis
data class InsertSUiState(
    val insertSUiEvent: InsertSUiEvent = InsertSUiEvent() // State default berisi objek kosong dari InsertUiEvent
)

// Menyimpan data input pengguna untuk form terapis
data class InsertSUiEvent(
    val id_sesi: String = "",
    val id_pasien: String = "",
    val id_terapis: String = "",
    val id_jenis_terapi: String = "",
    val tanggal_sesi: String = "",
    val catatan_sesi: String = ""
)

// Fungsi untuk mengubah data InsertUiEvent menjadi terapis
fun InsertSUiEvent.toSesi(): SesiTerapi = SesiTerapi( // InsertUiEvent > sesi terapi > Simpan data Sesi ke db
    id_sesi = id_sesi, // Memindahkan nilai ID dari InsertUiEvent ke sesi terapi
    id_pasien = id_pasien,
    id_terapis = id_terapis,
    id_jenis_terapi = id_jenis_terapi,
    tanggal_sesi = tanggal_sesi,
    catatan_sesi = catatan_sesi
)

// Fungsi untuk mengubah data sesi terapi menjadi InsertUiState
fun SesiTerapi.toInsertSUiStateSesi(): InsertSUiState = InsertSUiState( // sesi terapi > insertUiEvent > Masuk ke InsertUiState
    insertSUiEvent = toInsertSUiEvent() // Memanggil fungsi toInsertUiEvent untuk mengonversi data sesi terapi
)

// Fungsi untuk mengubah data sesi terapi menjadi data InsertUiEvent
fun SesiTerapi.toInsertSUiEvent(): InsertSUiEvent = InsertSUiEvent(
    id_sesi = id_sesi, // Memindahkan nilai ID dari sesi terapi ke InsertUiEvent
    id_pasien = id_pasien,
    id_terapis = id_terapis,
    id_jenis_terapi = id_jenis_terapi,
    tanggal_sesi = tanggal_sesi,
    catatan_sesi = catatan_sesi
)