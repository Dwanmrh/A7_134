package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.repository.JenisTerapiRepo
import kotlinx.coroutines.launch

class UpdateJenisTerapiViewModel(private val jns: JenisTerapiRepo) : ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var updateJUiState by mutableStateOf(UpdateJUiState())
        private set

    // Fungsi untuk mendapatkan data jenis terapi berdasarkan ID
    fun getJenisTerapiById(id_jenis_terapi: String) {
        viewModelScope.launch {
            try {
                val jenisTerapi = jns.getJenisById(id_jenis_terapi)
                updateJUiState = updateJUiState.copy(
                    updateJUiEvent = UpdateJUiEvent(
                        id_jenis_terapi = jenisTerapi.id_jenis_terapi,
                        nama_jenis_terapi = jenisTerapi.nama_jenis_terapi,
                        deskripsi_terapi = jenisTerapi.deskripsi_terapi
                    )
                )
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateJenisTerapiState(updateJUiEvent: UpdateJUiEvent) {
        updateJUiState = UpdateJUiState(updateJUiEvent = updateJUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk memuat data jenis terapi ke dalam form untuk di-update
    fun loadJenisTerapiData(jenisTerapi: JenisTerapi) {
        updateJUiState = jenisTerapi.toUpdateUiStateJenisTerapi()
    }

    // Fungsi untuk memperbarui data jenis terapi ke database
    fun updateJenisTerapi() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                jns.updateJenis(
                    id_jenis_terapi = updateJUiState.updateJUiEvent.id_jenis_terapi, // Ambil ID dari updateUiState
                    jenisTerapi = updateJUiState.updateJUiEvent.toJns() // Konversi event menjadi jenis terapi
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form update terapis
data class UpdateJUiState(
    val updateJUiEvent: UpdateJUiEvent = UpdateJUiEvent() // State default berisi objek kosong dari UpdateUiEvent
)

// Menyimpan data input pengguna untuk form update jenis terapi
data class UpdateJUiEvent(
    val id_jenis_terapi: String = "",
    val nama_jenis_terapi: String = "",
    val deskripsi_terapi: String = ""
)

// Fungsi untuk mengubah data UpdateUiEvent menjadi jenis terapi
fun UpdateJUiEvent.toJns(): JenisTerapi = JenisTerapi( // UpdateUiEvent > jenis terapi > Simpan data Pas ke db
    id_jenis_terapi = id_jenis_terapi, // Memindahkan nilai ID dari UpdateUiEvent ke jenis terapi
    nama_jenis_terapi = nama_jenis_terapi,
    deskripsi_terapi = deskripsi_terapi
)

// Fungsi untuk mengubah data jenis terapi menjadi UpdateUiState
fun JenisTerapi.toUpdateUiStateJenisTerapi(): UpdateJUiState = UpdateJUiState( // jenis terapi > updateUiEvent > Masuk ke UpdateUiState
    updateJUiEvent = toUpdateJUiEvent() // Memanggil fungsi toUpdateUiEvent untuk mengonversi data jenis terapi
)

// Fungsi untuk mengubah data jenis terapi menjadi data UpdateUiEvent
fun JenisTerapi.toUpdateJUiEvent(): UpdateJUiEvent = UpdateJUiEvent(
    id_jenis_terapi = id_jenis_terapi, // Memindahkan nilai ID dari jenis terapi ke UpdateUiEvent
    nama_jenis_terapi = nama_jenis_terapi,
    deskripsi_terapi = deskripsi_terapi
)