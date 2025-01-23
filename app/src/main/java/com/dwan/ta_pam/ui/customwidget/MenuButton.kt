package com.dwan.ta_pam.ui.customwidget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuButton(
    onPasienClick: () -> Unit,
    onTerapisClick: () -> Unit,
    onJenisTerapiClick: () -> Unit,
    onSesiTerapiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Tombol Pasien
        Button(onClick = onPasienClick) {
            Text("Pasien")
        }

        // Tombol Terapis
        Button(onClick = onTerapisClick) {
            Text("Terapis")
        }

        // Tombol Jenis Terapi
        Button(onClick = onJenisTerapiClick) {
            Text("Jenis Terapi")
        }

        // Tombol Sesi Terapi
        Button(onClick = onSesiTerapiClick) {
            Text("Sesi Terapi")
        }
    }
}
