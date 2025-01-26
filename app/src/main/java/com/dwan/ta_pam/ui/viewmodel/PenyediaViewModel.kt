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

        // Terapis ViewModel
        initializer { TerapisViewModel(KlinikApplication().container.terapisRepo) }
        initializer { InsertTerapisViewModel(KlinikApplication().container.terapisRepo) }
        initializer { UpdateTerapisViewModel(KlinikApplication().container.terapisRepo) }
        initializer { DetailTerapisViewModel(KlinikApplication().container.terapisRepo) }

        // Jenis Terapi ViewModel
        initializer { JenisTerapiViewModel(KlinikApplication().container.jenisTerapiRepo) }
        initializer { InsertJenisTerapiViewModel(KlinikApplication().container.jenisTerapiRepo) }
        initializer { UpdateJenisTerapiViewModel(KlinikApplication().container.jenisTerapiRepo) }
        initializer { DetailJenisTerapiViewModel(KlinikApplication().container.jenisTerapiRepo) }

        // Sesi Terapi ViewModel
        initializer { SesiTerapiViewModel(KlinikApplication().container.sesiTerapiRepo) }
        initializer { InsertSesiTerapiViewModel(KlinikApplication().container.sesiTerapiRepo) }
        initializer { UpdateSesiTerapiViewModel(KlinikApplication().container.sesiTerapiRepo) }
        initializer { DetailSesiTerapiViewModel(KlinikApplication().container.sesiTerapiRepo) }
    }
    fun CreationExtras.KlinikApplication(): KlinikApplication =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as KlinikApplication)
}