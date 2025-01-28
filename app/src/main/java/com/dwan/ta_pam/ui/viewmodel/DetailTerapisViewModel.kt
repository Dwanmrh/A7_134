package com.dwan.ta_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.repository.TerapisRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailTerapisViewModel(private val terapisRepo: TerapisRepo) : ViewModel() {

    private val _detailTerUiState = MutableStateFlow<DetailTerUiState>(DetailTerUiState.Loading)
    val detailTerUiState: StateFlow<DetailTerUiState> = _detailTerUiState.asStateFlow()

    fun getTerapisById(id_terapis: String) {
        viewModelScope.launch {
            try {
                val terapis = terapisRepo.getTerapisById(id_terapis)
                if (terapis != null) {
                    _detailTerUiState.value = DetailTerUiState.Success(terapis)
                } else {
                    _detailTerUiState.value = DetailTerUiState.Error("Data Terapis tidak ditemukan.")
                }
            } catch (e: Exception) {
                _detailTerUiState.value = DetailTerUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deleteTer(id_terapis: String) {
        viewModelScope.launch {
            try {
                terapisRepo.deleteTerapis(id_terapis)
                _detailTerUiState.value = DetailTerUiState.Error("Data Terapis telah dihapus.")
            } catch (e: Exception) {
                _detailTerUiState.value = DetailTerUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}

sealed class DetailTerUiState {
    object Loading : DetailTerUiState()
    data class Success(val terapis: Terapis) : DetailTerUiState()
    data class Error(val message: String) : DetailTerUiState()
}