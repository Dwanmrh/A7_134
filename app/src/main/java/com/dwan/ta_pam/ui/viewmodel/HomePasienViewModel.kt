package com.dwan.ta_pam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.repository.PasienRepo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeUiState{ // Digunakan untuk membatasi subclass yang dapat di-extend dari kelas ini.

    // Subclass Success
    data class Success(val pasien: List<Pasien>): HomeUiState()

    // Subclass Error berupa object. Menunjukkan bahwa terjadi kesalahan tanpa detail tambahan.
    object Error: HomeUiState()

    // Subclass Loading. Menunjukkan bahwa aplikasi sedang dalam proses memuat data.
    object Loading: HomeUiState()
}

class HomePasienViewModel(private val pas: PasienRepo): ViewModel() {

    var pasUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set // Setter-nya dibuat private agar state hanya dapat diubah oleh ViewModel.

    init {
        getPas()
    }

    fun getPas() {
        viewModelScope.launch {

            // Set state ke Loading untuk menunjukkan bahwa data sedang diproses.
            pasUiState = HomeUiState.Loading

            // Mencoba mengambil data pasien dari repository menggunakan blok try-catch.
            pasUiState = try {

                // Jika berhasil, state diubah menjadi Success dengan daftar pasien sebagai datanya.
                HomeUiState.Success(pas.getPasien())
            }catch (e: IOException) {

                // Jika terjadi kesalahan jaringan atau I/O, set state ke Error.
                HomeUiState.Error
            }catch (e: HttpException) {

                // Jika terjadi kesalahan HTTP (misalnya, 404 atau 500), set state ke Error.
                HomeUiState.Error
            }
        }
    }

    fun deletePas(id_pasien: String) {
        viewModelScope.launch {

            // Menggunakan blok try-catch untuk menangani kemungkinan kesalahan selama proses penghapusan.
            try {

                // Memanggil fungsi delete Pasien pada repository untuk menghapus data pasien berdasarkan ID Pasien.
                pas.deletePasien(id_pasien)
            }catch (e: IOException) {
                HomeUiState.Error
            }catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }
}