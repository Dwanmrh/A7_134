package com.dwan.ta_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.repository.PasienRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPasienViewModel(private val pasienRepo: PasienRepo) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    fun getPasienById(id_pasien: String) {
        viewModelScope.launch {
            try {
                val pasien = pasienRepo.getPasienById(id_pasien)
                if (pasien != null) {
                    _detailUiState.value = DetailUiState.Success(pasien)
                } else {
                    _detailUiState.value = DetailUiState.Error("Data Pasien tidak ditemukan.")
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deletePas(id_pasien: String) {
        viewModelScope.launch {
            try {
                pasienRepo.deletePasien(id_pasien)
                _detailUiState.value = DetailUiState.Error("Data Pasien telah dihapus.")
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val pasien: Pasien) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}