package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.InsertTUiEvent
import com.dwan.ta_pam.ui.viewmodel.InsertTUiState
import com.dwan.ta_pam.ui.viewmodel.InsertTerapisViewModel
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// Membuat objek `DestinasiEntryTerapis` yang mengimplementasikan interface `DestinasiNavigasi`
object DestinasiEntryTerapis : DestinasiNavigasi {

    // Mendefinisikan properti `route` yang mengacu pada string "item_entry"
    override val route = "entry_terapis"

    // Mendefinisikan properti `titleRes` yang mengacu pada string "Entry Pas"
    override val titleRes = "Insert Terapis"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTerapisScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertTerapisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryTerapis.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        EntryBodyTerapis(
            insertTUiState = viewModel.insertTUiState,
            onTerapisValueChange = viewModel::updateInsertTerState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertTer()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryBodyTerapis(
    insertTUiState: InsertTUiState,
    onTerapisValueChange: (InsertTUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputTerapis(
            insertTUiEvent = insertTUiState.insertTUiEvent,
            onValueChange = onTerapisValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B0000), // Warna merah gelap
                contentColor = Color.White // Warna teks putih
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputTerapis(
    insertTUiEvent: InsertTUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertTUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertTUiEvent.nama_terapis,
            onValueChange = { onValueChange(insertTUiEvent.copy(nama_terapis = it)) },
            label = { Text("Nama Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertTUiEvent.id_terapis,
            onValueChange = { onValueChange(insertTUiEvent.copy(id_terapis = it)) },
            label = { Text("ID Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertTUiEvent.spesialisasi,
            onValueChange = { onValueChange(insertTUiEvent.copy(spesialisasi = it)) },
            label = { Text("Spesialisasi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertTUiEvent.nomor_izin_praktik,
            onValueChange = { onValueChange(insertTUiEvent.copy(nomor_izin_praktik = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Nomor Izin Praktik") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Menampilkan pesan "Isi Semua Data!" jika input aktif
        if (enabled) {
            Text(
                text = "Harap Mengisi Semua Data!",
                modifier = Modifier.padding(12.dp),
                color = Color.Blue
            )
        }
        // Garis pemisah tebal
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}