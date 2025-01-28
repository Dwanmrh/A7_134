package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.repository.SesiTerapiRepo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeSUiState{ // Digunakan untuk membatasi subclass yang dapat di-extend dari kelas ini.

    // Subclass Success
    data class Success(val sesiTerapi: List<SesiTerapi>): HomeSUiState()

    // Subclass Error berupa object. Menunjukkan bahwa terjadi kesalahan tanpa detail tambahan.
    object Error: HomeSUiState()

    // Subclass Loading. Menunjukkan bahwa aplikasi sedang dalam proses memuat data.
    object Loading: HomeSUiState()
}

class SesiTerapiViewModel(private val sesi: SesiTerapiRepo): ViewModel() {

    // sesiUiState digunakan untuk menyimpan keadaan UI (state) sesi terapi.
    // mutableStateOf digunakan untuk membuat state yang dapat berubah dan otomatis memicu pembaruan UI ketika nilainya berubah.
    // State awalnya diset ke HomeUiState.Loading.
    var sesiUiState: HomeSUiState by mutableStateOf(HomeSUiState.Loading)
        private set // Setter-nya dibuat private agar state hanya dapat diubah oleh ViewModel.

    init {
        getSesi()
    }

    fun getSesi() {
        viewModelScope.launch {

            // Set state ke Loading untuk menunjukkan bahwa data sedang diproses.
            sesiUiState = HomeSUiState.Loading

            // Mencoba mengambil data sesi terapi dari repository menggunakan blok try-catch.
            sesiUiState = try {

                // Jika berhasil, state diubah menjadi Success dengan daftar sesi terapi sebagai datanya.
                HomeSUiState.Success(sesi.getSesi())
            }catch (e: IOException) {

                // Jika terjadi kesalahan jaringan atau I/O, set state ke Error.
                HomeSUiState.Error
            }catch (e: HttpException) {

                // Jika terjadi kesalahan HTTP (misalnya, 404 atau 500), set state ke Error.
                HomeSUiState.Error
            }
        }
    }

    fun deleteSesi(id_sesi: String) {
        viewModelScope.launch {

            // Menggunakan blok try-catch untuk menangani kemungkinan kesalahan selama proses penghapusan.
            try {

                // Memanggil fungsi delete sesi terapi pada repository untuk menghapus data sesi terapi berdasarkan ID.
                sesi.deleteSesi(id_sesi)
            }catch (e: IOException) {
                HomeSUiState.Error
            }catch (e: HttpException) {
                HomeSUiState.Error
            }
        }
    }
}