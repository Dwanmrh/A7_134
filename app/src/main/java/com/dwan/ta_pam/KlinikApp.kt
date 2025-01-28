package com.dwan.ta_pam

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dwan.ta_pam.ui.navigation.PengelolaHalaman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlinikApp(modifier: Modifier = Modifier) {
    // Menampilkan halaman dengan navigasi
        PengelolaHalaman()
}