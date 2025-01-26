package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.repository.TerapisRepo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeTUiState{ // Digunakan untuk membatasi subclass yang dapat di-extend dari kelas ini.

    // Subclass Success
    data class Success(val terapis: List<Terapis>): HomeTUiState()

    // Subclass Error berupa object. Menunjukkan bahwa terjadi kesalahan tanpa detail tambahan.
    object Error: HomeTUiState()

    // Subclass Loading. Menunjukkan bahwa aplikasi sedang dalam proses memuat data.
    object Loading: HomeTUiState()
}

class TerapisViewModel(private val trps: TerapisRepo): ViewModel() {

    // mhsUiState digunakan untuk menyimpan keadaan UI (state) mahasiswa.
    // mutableStateOf digunakan untuk membuat state yang dapat berubah dan otomatis memicu pembaruan UI ketika nilainya berubah.
    // State awalnya diset ke HomeUiState.Loading.
    var terUiState: HomeTUiState by mutableStateOf(HomeTUiState.Loading)
        private set // Setter-nya dibuat private agar state hanya dapat diubah oleh ViewModel.

    init {
        getTer()
    }

    fun getTer() {
        viewModelScope.launch {

            // Set state ke Loading untuk menunjukkan bahwa data sedang diproses.
            terUiState = HomeTUiState.Loading

            // Mencoba mengambil data mahasiswa dari repository menggunakan blok try-catch.
            terUiState = try {

                // Jika berhasil, state diubah menjadi Success dengan daftar mahasiswa sebagai datanya.
                HomeTUiState.Success(trps.getTerapis())
            }catch (e: IOException) {

                // Jika terjadi kesalahan jaringan atau I/O, set state ke Error.
                HomeTUiState.Error
            }catch (e: HttpException) {

                // Jika terjadi kesalahan HTTP (misalnya, 404 atau 500), set state ke Error.
                HomeTUiState.Error
            }
        }
    }

    fun deleteTer(id_terapis: String) {
        viewModelScope.launch {

            // Menggunakan blok try-catch untuk menangani kemungkinan kesalahan selama proses penghapusan.
            try {

                // Memanggil fungsi deleteMahasiswa pada repository untuk menghapus data mahasiswa berdasarkan NIM.
                trps.deleteTerapis(id_terapis)
            }catch (e: IOException) {
                HomeTUiState.Error
            }catch (e: HttpException) {
                HomeTUiState.Error
            }
        }
    }
}