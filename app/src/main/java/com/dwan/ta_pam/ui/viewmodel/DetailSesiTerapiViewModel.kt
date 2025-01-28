package com.dwan.ta_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.repository.SesiTerapiRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailSesiTerapiViewModel(private val sesiTerapiRepo: SesiTerapiRepo) : ViewModel() {

    private val _detailSsiUiState = MutableStateFlow<DetailSsiUiState>(DetailSsiUiState.Loading)
    val detailSsiUiState: StateFlow<DetailSsiUiState> = _detailSsiUiState.asStateFlow()

    fun getSesiTerapiById(id_sesi: String) {
        viewModelScope.launch {
            try {
                val sesiTerapi = sesiTerapiRepo.getSesiById(id_sesi)
                if (sesiTerapi != null) {
                    _detailSsiUiState.value = DetailSsiUiState.Success(sesiTerapi)
                } else {
                    _detailSsiUiState.value = DetailSsiUiState.Error("Data Sesi Terapi tidak ditemukan.")
                }
            } catch (e: Exception) {
                _detailSsiUiState.value = DetailSsiUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deleteSsi(id_sesi: String) {
        viewModelScope.launch {
            try {
                sesiTerapiRepo.deleteSesi(id_sesi)
                _detailSsiUiState.value = DetailSsiUiState.Error("Data Sesi Terapi telah dihapus.")
            } catch (e: Exception) {
                _detailSsiUiState.value = DetailSsiUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}

sealed class DetailSsiUiState {
    object Loading : DetailSsiUiState()
    data class Success(val sesiTerapi: SesiTerapi) : DetailSsiUiState()
    data class Error(val message: String) : DetailSsiUiState()
}