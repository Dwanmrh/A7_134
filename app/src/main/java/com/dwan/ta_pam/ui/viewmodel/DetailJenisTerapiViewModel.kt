package com.dwan.ta_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.repository.JenisTerapiRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailJenisTerapiViewModel(private val jenisTerapiRepo: JenisTerapiRepo) : ViewModel() {

    private val _detailJnsUiState = MutableStateFlow<DetailJnsUiState>(DetailJnsUiState.Loading)
    val detailJnsUiState: StateFlow<DetailJnsUiState> = _detailJnsUiState.asStateFlow()

    fun getJenisTerapiById(id_jenis_terapi: String) {
        viewModelScope.launch {
            try {
                val jenisTerapi = jenisTerapiRepo.getJenisById(id_jenis_terapi)
                if (jenisTerapi != null) {
                    _detailJnsUiState.value = DetailJnsUiState.Success(jenisTerapi)
                } else {
                    _detailJnsUiState.value = DetailJnsUiState.Error("Data Jenis Terapi tidak ditemukan.")
                }
            } catch (e: Exception) {
                _detailJnsUiState.value = DetailJnsUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deleteJns(id_jenis_terapi: String) {
        viewModelScope.launch {
            try {
                jenisTerapiRepo.deleteJenis(id_jenis_terapi)
                _detailJnsUiState.value = DetailJnsUiState.Error("Data Jenis Terapi telah dihapus.")
            } catch (e: Exception) {
                _detailJnsUiState.value = DetailJnsUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}

sealed class DetailJnsUiState {
    object Loading : DetailJnsUiState()
    data class Success(val jenisTerapi: JenisTerapi) : DetailJnsUiState()
    data class Error(val message: String) : DetailJnsUiState()
}