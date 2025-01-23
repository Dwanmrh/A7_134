package com.dwan.ta_pam.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dwan.ta_pam.KlinikApplication

object PenyediaViewModel {
    val Factory = viewModelFactory{
        // Pasien ViewModel
        initializer { HomePasienViewModel(KlinikApplication().container.pasienRepo) }
        initializer { InsertPasienViewModel(KlinikApplication().container.pasienRepo) }
        initializer { UpdatePasienViewModel(KlinikApplication().container.pasienRepo) }
        initializer { DetailPasienViewModel(KlinikApplication().container.pasienRepo) }
    }
    fun CreationExtras.KlinikApplication(): KlinikApplication =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as KlinikApplication)
}