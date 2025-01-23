package com.dwan.ta_pam

import android.app.Application
import com.dwan.ta_pam.repository.AppContainer
import com.dwan.ta_pam.repository.DataKlinikContainer

class KlinikApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DataKlinikContainer()
    }
}