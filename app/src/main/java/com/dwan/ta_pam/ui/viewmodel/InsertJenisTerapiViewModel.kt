package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.repository.JenisTerapiRepo
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form tambah jenis terapi
class InsertJenisTerapiViewModel(private val jns: JenisTerapiRepo): ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var insertJUiState by mutableStateOf(InsertJUiState())
        private set

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateInsertJnsState(insertJnsUiEvent: InsertJUiEvent) {
        insertJUiState = InsertJUiState(insertJUiEvent = insertJnsUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk menambahkan data jenis terapi ke database
    suspend fun insertJns() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                jns.insertJenis(insertJUiState.insertJUiEvent.toJns())
            }catch (e:Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form input terapis
data class InsertJUiState(
    val insertJUiEvent: InsertJUiEvent = InsertJUiEvent() // State default berisi objek kosong dari InsertUiEvent
)

// Menyimpan data input pengguna untuk form terapis
data class InsertJUiEvent(
    val id_jenis_terapi: String = "",
    val nama_jenis_terapi: String = "",
    val deskripsi_terapi: String = ""
)

// Fungsi untuk mengubah data InsertUiEvent menjadi terapis
fun InsertJUiEvent.toJns(): JenisTerapi = JenisTerapi( // InsertUiEvent > jenis terapi > Simpan data Jns ke db
    id_jenis_terapi = id_jenis_terapi, // Memindahkan nilai ID dari InsertUiEvent ke jenis terapi
    nama_jenis_terapi = nama_jenis_terapi,
    deskripsi_terapi = deskripsi_terapi,
)

// Fungsi untuk mengubah data jenis terapi menjadi InsertUiState
fun JenisTerapi.toInsertJUiStateJns(): InsertJUiState = InsertJUiState( // jenis terapi > insertUiEvent > Masuk ke InsertUiState
    insertJUiEvent = toInsertJUiEvent() // Memanggil fungsi toInsertUiEvent untuk mengonversi data jenis terapi
)

// Fungsi untuk mengubah data jenis terapi menjadi data InsertUiEvent
fun JenisTerapi.toInsertJUiEvent(): InsertJUiEvent = InsertJUiEvent(
    id_jenis_terapi = id_jenis_terapi, // Memindahkan nilai ID dari jenis terapi ke InsertUiEvent
    nama_jenis_terapi = nama_jenis_terapi,
    deskripsi_terapi = deskripsi_terapi
)