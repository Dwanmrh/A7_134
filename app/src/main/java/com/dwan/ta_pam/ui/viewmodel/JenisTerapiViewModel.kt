package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.repository.JenisTerapiRepo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeJUiState{ // Digunakan untuk membatasi subclass yang dapat di-extend dari kelas ini.

    // Subclass Success
    data class Success(val jenisTerapi: List<JenisTerapi>): HomeJUiState()

    // Subclass Error berupa object. Menunjukkan bahwa terjadi kesalahan tanpa detail tambahan.
    object Error: HomeJUiState()

    // Subclass Loading. Menunjukkan bahwa aplikasi sedang dalam proses memuat data.
    object Loading: HomeJUiState()
}

class JenisTerapiViewModel(private val jns: JenisTerapiRepo): ViewModel() {

    // jnsUiState digunakan untuk menyimpan keadaan UI (state) jenis terapi.
    // mutableStateOf digunakan untuk membuat state yang dapat berubah dan otomatis memicu pembaruan UI ketika nilainya berubah.
    // State awalnya diset ke HomeUiState.Loading.
    var jnsUiState: HomeJUiState by mutableStateOf(HomeJUiState.Loading)
        private set // Setter-nya dibuat private agar state hanya dapat diubah oleh ViewModel.

    init {
        getJns()
    }

    fun getJns() {
        viewModelScope.launch {

            // Set state ke Loading untuk menunjukkan bahwa data sedang diproses.
            jnsUiState = HomeJUiState.Loading

            // Mencoba mengambil data jenis terapi dari repository menggunakan blok try-catch.
            jnsUiState = try {

                // Jika berhasil, state diubah menjadi Success dengan daftar jenis terapi sebagai datanya.
                HomeJUiState.Success(jns.getJenis())
            }catch (e: IOException) {

                // Jika terjadi kesalahan jaringan atau I/O, set state ke Error.
                HomeJUiState.Error
            }catch (e: HttpException) {

                // Jika terjadi kesalahan HTTP (misalnya, 404 atau 500), set state ke Error.
                HomeJUiState.Error
            }
        }
    }

    fun deleteJns(id_jenis_terapi: String) {
        viewModelScope.launch {

            // Menggunakan blok try-catch untuk menangani kemungkinan kesalahan selama proses penghapusan.
            try {

                // Memanggil fungsi jenis terapi pada repository untuk menghapus data jenis terapi berdasarkan ID.
                jns.deleteJenis(id_jenis_terapi)
            }catch (e: IOException) {
                HomeTUiState.Error
            }catch (e: HttpException) {
                HomeTUiState.Error
            }
        }
    }
}